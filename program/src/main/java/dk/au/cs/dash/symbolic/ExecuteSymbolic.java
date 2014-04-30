package dk.au.cs.dash.symbolic;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import dk.au.cs.dash.Parameter;
import dk.au.cs.dash.PreparedMethod;
import dk.au.cs.dash.PreparedProgram;
import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.cfg.Op.*;
import dk.au.cs.dash.cfg.Region;
import dk.au.cs.dash.instrumentation.State;
import dk.au.cs.dash.trace.ConcreteTraceWithAbstractFrontier;
import dk.au.cs.dash.trace.RegionState;
import dk.au.cs.dash.util.DashConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class ExecuteSymbolic {
    private static final String INITIAL_SYMBOLIC_VARIABLE_SUFFIX = "ₒ";
    private final ArrayList<Parameter> parameters;
    private final Graph g;
    private final ConcreteTraceWithAbstractFrontier trace;
    private final Context ctx;
    private final PreparedProgram preparedProgram;

    private final ArrayList<BoolExpr> pathConstraint = new ArrayList<>();
    private final ArrayList<Boolean> nonDetChoices = new ArrayList<>();

    public ExecuteSymbolic(ArrayList<Parameter> parameters, Graph g, ConcreteTraceWithAbstractFrontier trace, Context ctx, PreparedProgram preparedProgram) {
        this.parameters = requireNonNull(parameters);
        this.preparedProgram = requireNonNull(preparedProgram);
        this.ctx = requireNonNull(ctx);
        this.g = requireNonNull(g);
        this.trace = requireNonNull(trace);
    }

    public static String getInitialSymbolicVariableName(String name) {
        return name + INITIAL_SYMBOLIC_VARIABLE_SUFFIX;
    }

    public static String removeSuffixFromInitialSymbolicVariableName(String name) {
        if(!isInitialSymbolicVariable(name))
            throw new RuntimeException();
        return name.substring(0, name.length() - INITIAL_SYMBOLIC_VARIABLE_SUFFIX.length());
    }

    public static boolean isInitialSymbolicVariable(String name) {
        return name.endsWith(INITIAL_SYMBOLIC_VARIABLE_SUFFIX);
    }

    public PathConstraintAndState getPathConstraint() {
        SymbolicMap symbolicMapUpToFrontier = new SymbolicMap();

        for (Parameter parameter : parameters) {
            symbolicMapUpToFrontier.put(ctx.mkBVConst(parameter.name, DashConstants.BITS_IN_INT), ctx.mkBVConst(getInitialSymbolicVariableName(parameter.name), DashConstants.BITS_IN_INT));
        }

        Region beforeFrontier = trace.beforeFrontier.region;
        Region afterFrontier = trace.afterFrontier.region;

        executeSymbolicRegion(trace.trace.get(0).region, symbolicMapUpToFrontier);

        for (int i = 0; i < trace.trace.size() - 2; i++) {
            RegionState regionState = trace.trace.get(i);
            RegionState after = trace.trace.get(i + 1);
            Op op = g.getOp(regionState.region, after.region);

            executeStatement(op, symbolicMapUpToFrontier, regionState.state);
            executeSymbolicRegion(after.region, symbolicMapUpToFrontier);
        }

        Op op = g.getOp(beforeFrontier, afterFrontier);
        executeFrontierAndRegion(op, trace.afterFrontier.region, symbolicMapUpToFrontier);

        BoolExpr pathConstraint = makeAnd(this.pathConstraint, ctx);
        return new PathConstraintAndState(pathConstraint, symbolicMapUpToFrontier, nonDetChoices);
    }

    private void executeSymbolicRegion(Region region, SymbolicMap symbolicMap) {
        VariableInserter inserter = new VariableInserter(symbolicMap);
        Expr result = inserter.insertValueInto(region.predicate).simplify();

	    if(!result.isTrue())
            pathConstraint.add((BoolExpr)result);
    }

    private void executeStatement(Op op, SymbolicMap symbolicMap, State previousState) {

        if (op instanceof AssignOp) {
            executeAssignment((AssignOp) op, symbolicMap);
        } else if (op instanceof FakeAssumeTrueOp) {
            executeNondetChoice((FakeAssumeTrueOp) op);
        } else if (op instanceof AssumeOp) {
            executeAssume((AssumeOp) op, symbolicMap);
        } else if (op instanceof AssignWithCallOp) {
            executeSymbolicInvoke((AssignWithCallOp) op, symbolicMap, previousState);
        } else if (op instanceof CallOp) {
            throw new RuntimeException("Execute symbolic call op");
        } else {
            throw new RuntimeException(op.toString());
        }
    }

    private void executeFrontierAndRegion(Op op, Region region, SymbolicMap symbolicMap) {
        if (op instanceof AssignOp) {
            SymbolicMap symbolicMapIncludingFrontier = symbolicMap.createCopy();
            executeAssignment((AssignOp) op, symbolicMapIncludingFrontier);
            executeSymbolicRegion(region, symbolicMapIncludingFrontier);
        } else if (op instanceof FakeAssumeTrueOp) {
            executeNondetChoice((FakeAssumeTrueOp) op);
            executeSymbolicRegion(region, symbolicMap);
        } else if (op instanceof AssumeOp) {
            executeAssume((AssumeOp) op, symbolicMap);
            executeSymbolicRegion(region, symbolicMap);
        } else if (op instanceof ErrorOp) {
            // Do nothing, HACK for when Dash.Error is the first statement in a method
        } else if (op instanceof ReturnOp) {
            // Return statement is encoded in the exit constraints, e.i. the region predicate
            executeSymbolicRegion(region, symbolicMap);
        } else if (op instanceof CallOp) {
            // Do nothing
        } else if (op instanceof AssignWithCallOp) {
            // Do nothing
        } else {
            throw new RuntimeException(op.toString());
        }
    }

    private void executeAssume(AssumeOp op, SymbolicMap symbolicMap) {
        VariableInserter symbolicMapInserter = new VariableInserter(symbolicMap);
        BoolExpr inserted = symbolicMapInserter.insertValueInto(op.assumption);

        if(!inserted.isTrue())
            pathConstraint.add(inserted);
    }

    private static void executeAssignment(AssignOp aOp, SymbolicMap symbolicMap) {
        VariableInserter symbolicMapInserter = new VariableInserter(symbolicMap);
        BitVecExpr rvalue = symbolicMapInserter.insertValueInto(aOp.rvalue);
        symbolicMap.put(aOp.name, rvalue);
    }

    private void executeNondetChoice(FakeAssumeTrueOp op) {
        FakeAssumeTrueOp.BranchType branchType = op.getBranchType();
        switch (branchType) {
            case BRANCH_EDGE_TRUE:
                nonDetChoices.add(true);
                break;
            case BRANCH_EDGE_FALSE:
                nonDetChoices.add(false);
                break;
            case NOT_A_BRANCH_EDGE:
                break;
        }
    }

    private void executeSymbolicInvoke(AssignWithCallOp op, SymbolicMap callSiteSymbolicMap, State stateBeforeCallState) {
        CallOp callOp = op.callOp;
        PreparedMethod preparedMethod = preparedProgram.get(callOp.className, callOp.methodName);
        SymbolicMap calledMethodSymbolicMap = connectArgumentsWithParameters(callOp.arguments, preparedMethod.parameters, callSiteSymbolicMap);

        //Find the state that follows from the trace at the call site (runId and state.index + 1)
        State startStateInInvokedMethod = findStartStateInInvokedMethod(stateBeforeCallState, preparedMethod.g.entryPoint);
        BitVecExpr symbolicReturnValue = executeSubMethod(startStateInInvokedMethod, preparedMethod.g, calledMethodSymbolicMap);

        callSiteSymbolicMap.put(op.localName, symbolicReturnValue);
    }

    private BitVecExpr executeSubMethod(State startStateInInvokedMethod, Graph g, SymbolicMap symbolicMap) {

        State previousState = startStateInInvokedMethod;
        Region previousRegion = g.entryPoint;
        while(true) {
            State nextState = previousState.child;
            Region nextRegion = findRegionWithState(g.edges.getChildrenOf(previousRegion), nextState);
            Op op = g.getOp(previousRegion, nextRegion);

            if(op instanceof ReturnOp) {
                BitVecExpr returnVariable = ((ReturnOp) op).getReturnVariable();
                VariableInserter symbolicMapInserter = new VariableInserter(symbolicMap);
                return symbolicMapInserter.insertValueInto(returnVariable);
            }

            executeStatement(op, symbolicMap, previousState);

            previousState = nextState;
            previousRegion = nextRegion;
        }
    }

    private SymbolicMap connectArgumentsWithParameters(List<BitVecExpr> arguments, ArrayList<Parameter> parameters, SymbolicMap callSiteSymbolicMap) {

        VariableInserter symbolicMapAtCallSiteInserter = new VariableInserter(callSiteSymbolicMap);
        SymbolicMap currentMethodSymbolicMap = new SymbolicMap();
        for (int i = 0; i < arguments.size(); i++) {
            BitVecExpr param = ctx.mkBVConst(parameters.get(i).name, DashConstants.BITS_IN_INT);
            BitVecExpr symbolicArgument = symbolicMapAtCallSiteInserter.insertValueInto(arguments.get(i));

            currentMethodSymbolicMap.put(param, symbolicArgument);
        }

        return currentMethodSymbolicMap;
    }

    private static BoolExpr makeAnd(ArrayList<BoolExpr> predicates, Context ctx) {
        return ctx.mkAnd(predicates.toArray(new BoolExpr[predicates.size()]));
    }

    private static State findStartStateInInvokedMethod(State stateBeforeCallState, Region entryRegion) {
        for (State state : entryRegion.getStates()) {
            if (state.runId == stateBeforeCallState.runId &&
                    state.index == stateBeforeCallState.index + 1) {
                return state;
            }
        }
        throw new RuntimeException();
    }

    private static Region findRegionWithState(Set<Region> regions, State state) {
        for (Region r : regions) {
            if (r.getStates().contains(state)) {
                return r;
            }
        }
        throw new RuntimeException();
    }

    public static class PathConstraintAndState {
        public final BoolExpr pathConstraint;
        public final SymbolicMap symbolicMap;
        public final ArrayList<Boolean> nonDetChoices;

        public PathConstraintAndState(BoolExpr pathConstraint, SymbolicMap symbolicMap, ArrayList<Boolean> nonDetChoices) {
            this.pathConstraint = requireNonNull(pathConstraint);
            this.symbolicMap = requireNonNull(symbolicMap);
            this.nonDetChoices = requireNonNull(nonDetChoices);
        }
    }
}
