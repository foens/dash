package dk.au.cs.dash.symbolic;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.*;
import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.cfg.Op.AssignWithCallOp;
import dk.au.cs.dash.cfg.Op.CallOp;
import dk.au.cs.dash.cfg.Op.Op;
import dk.au.cs.dash.instrumentation.Variables;
import dk.au.cs.dash.trace.ConcreteTraceWithAbstractFrontier;
import dk.au.cs.dash.util.DashConstants;
import dk.au.cs.dash.util.SubstitutionArrays;
import dk.au.cs.dash.util.TimeTracer;
import dk.au.cs.dash.util.Z3Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

public class ExtendFrontier {
    public static final String MAGIC_PREFIX_SEPARATOR = "↓";

    private final ConcreteTraceWithAbstractFrontier trace;
    private final ArrayList<Parameter> parameters;
    private final Graph g;
    private final Dash dash;
    private final Context ctx;
    private final PreparedProgram preparedProgram;
    private static final AtomicInteger uniqueNumber = new AtomicInteger();
    private final ArrayList<String> paramAndLocalNamesForOuterMethod;

    public ExtendFrontier(ConcreteTraceWithAbstractFrontier trace, PreparedMethod method, Graph g, Dash dash, Context ctx, PreparedProgram preparedProgram) {
        this.trace = requireNonNull(trace);
        this.parameters = requireNonNull(method).parameters;
        this.paramAndLocalNamesForOuterMethod = requireNonNull(method).paramAndLocalNames;
        this.g = requireNonNull(g);
        this.dash = dash;
        this.ctx = requireNonNull(ctx);
        this.preparedProgram = requireNonNull(preparedProgram);
    }

    public ExtendFrontierResult getTraceOrPredicate() {
        TimeTracer.start("ExecuteSymbolic");
        ExecuteSymbolic.PathConstraintAndState pathConstraintAndState = new ExecuteSymbolic(parameters, g, trace, ctx, preparedProgram).getPathConstraint();
        TimeTracer.end("ExecuteSymbolic");

        Op op = g.edges.get(trace.beforeFrontier.region, trace.afterFrontier.region).op;
        if(op instanceof CallOp) {
            throw new RuntimeException("Refine over call op");
        }else if(op instanceof AssignWithCallOp) {
            AssignWithCallOp assignWithCallOp = (AssignWithCallOp) op;
            CallOp callOp = assignWithCallOp.callOp;
            String prefix = computeUniquePrefix();
            PreparedMethod subMethod = preparedProgram.get(callOp.className, callOp.methodName);
            VariableRenamer callersLocalsRenamer = createRenamer(prefix);

            BoolExpr inputConstraints = makeInputConstraints(callOp.arguments, subMethod.parameters, pathConstraintAndState.pathConstraint, pathConstraintAndState.symbolicMap, callersLocalsRenamer);
            BoolExpr exitConstraints = makeExitConstraints(trace.afterFrontier.region.predicate, assignWithCallOp.localName, callersLocalsRenamer);

            DashResult submethodResult = dash.dashLoop(callOp.className, callOp.methodName, inputConstraints, exitConstraints);

            if(!submethodResult.pass) {
                return extractTestInputForOuterProcedure(submethodResult, paramAndLocalNamesForOuterMethod, subMethod.parameters, subMethod.paramAndLocalNames, prefix, pathConstraintAndState.nonDetChoices);
            } else {
                return computeRefinePredicate(submethodResult.g.initialRegionSplitPredicates, callOp.arguments, subMethod.parameters, callersLocalsRenamer);
            }
        } else {
            Variables solution = isSat(pathConstraintAndState.pathConstraint);
            if (solution != null) {
                return new ExtendFrontierResult(solution, pathConstraintAndState.nonDetChoices);
            } else {
                BoolExpr predicate = refinePred(g, trace);
                return new ExtendFrontierResult(predicate);
            }
        }
    }

    private VariableRenamer createRenamer(String prefix) {
        ArrayList<String> toRename = new ArrayList<>(paramAndLocalNamesForOuterMethod);
        for (Parameter param_0 : parameters)
            toRename.add(ExecuteSymbolic.getInitialSymbolicVariableName(param_0.name));
        return new VariableRenamer(prefix, toRename, ctx);
    }

    private Variables isSat(BoolExpr pathConstraint) {
        return new SATSolver(pathConstraint, parameters, ctx).run();
    }

    private BoolExpr refinePred(Graph g, ConcreteTraceWithAbstractFrontier orderedTrace) {
        TimeTracer.start("RefinePred");
        BoolExpr predicate = new RefinePredicate(g, orderedTrace, ctx).getPredicate();
        TimeTracer.end("RefinePred");
        return predicate;
    }

    private ExtendFrontierResult extractTestInputForOuterProcedure(DashResult submethodResult, ArrayList<String> paramAndLocalNamesForOuterMethod, ArrayList<Parameter> parametersForSubMethod, ArrayList<String> paramAndLocalNamesForSubMethod, String outerLocalsPrefix, ArrayList<Boolean> nonDetChoicesBeforeSubMethod) {
        TimeTracer.start("extractTestInputForOuterProcedure");
        
        ArrayList<Boolean> combinedNondetChoice = new ArrayList<>();
        combinedNondetChoice.addAll(nonDetChoicesBeforeSubMethod);
        combinedNondetChoice.addAll(submethodResult.nondetChoices);

        Variables variables = submethodResult.input;
        Variables newVariables = new Variables();
        for (Map.Entry<String, Integer> entry : variables.entrySet()) {
            String name = entry.getKey();
            Integer value = entry.getValue();

            if(name.contains(MAGIC_PREFIX_SEPARATOR)) {
                if(name.startsWith(outerLocalsPrefix)) {
                    String unprefixedName = name.substring(outerLocalsPrefix.length());

                    if(ExecuteSymbolic.isInitialSymbolicVariable(unprefixedName)
                            && isContainedInParameters(ExecuteSymbolic.removeSuffixFromInitialSymbolicVariableName(unprefixedName), this.parameters)) {
                        newVariables.put(unprefixedName, value);
                    } else if(paramAndLocalNamesForOuterMethod.contains(unprefixedName)) {
                        //Ignore parameter and local variables
                    }else {
                        throw new RuntimeException("Unknown value " + name + " = " + value);
                    }

                } else {
                    newVariables.put(name, value);
                }
            } else {
                if(paramAndLocalNamesForSubMethod.contains(name) && !isContainedInParameters(name, parametersForSubMethod)) {
                    throw new RuntimeException(name);       //We should never see a local variable in the returned map
                } else if(isContainedInParameters(name, parametersForSubMethod)) {
                    throw new RuntimeException(name); // There should not be any parameters returned
                } else if(ExecuteSymbolic.isInitialSymbolicVariable(name) && isContainedInParameters(ExecuteSymbolic.removeSuffixFromInitialSymbolicVariableName(name), parametersForSubMethod)) {
                    // Ignore parameters_0 for sub method
                } else {
                    throw new RuntimeException("Unknown name for sub procedure " + name);
                }
            }
        }


        for (Parameter parameter : this.parameters) {
            String initialSymbolicVariableName = ExecuteSymbolic.getInitialSymbolicVariableName(parameter.name);
            if (!newVariables.contains(initialSymbolicVariableName))
                newVariables.put(initialSymbolicVariableName, 0);
        }
        TimeTracer.end("extractTestInputForOuterProcedure");
        return new ExtendFrontierResult(newVariables, combinedNondetChoice);
    }

    private static boolean isContainedInParameters(String name, List<Parameter> parameters)
    {
        for (Parameter parameter : parameters) {
            if(parameter.name.equals(name))
                return true;
        }
        return false;
    }

    private BoolExpr makeInputConstraints(List<BitVecExpr> arguments, ArrayList<Parameter> parameter, BoolExpr o1, SymbolicMap symbolicMap, VariableRenamer outerVariableRenamer) {
        BoolExpr[] inputConstraints = new BoolExpr[arguments.size() + 2];

        for(int i = 0; i < arguments.size(); ++i) {
            BitVecExpr parameterName = ctx.mkBVConst(parameter.get(i).name, DashConstants.BITS_IN_INT);

            BitVecExpr argRenamed = outerVariableRenamer.rename(arguments.get(i));
            inputConstraints[i] = ctx.mkEq(parameterName, argRenamed);
        }
        inputConstraints[inputConstraints.length - 2] = outerVariableRenamer.rename(symbolicMap.createPredicates(ctx));
        inputConstraints[inputConstraints.length - 1] = outerVariableRenamer.rename(o1);      //Insert path constraint!
        return ctx.mkAnd(inputConstraints);
    }

    private BoolExpr makeExitConstraints(BoolExpr skPredicate, BitVecExpr assignToLocalName, VariableRenamer outerVariableRenamer) {
        BoolExpr substituted = VariableInserter.createExitConstraintsInserter(assignToLocalName, ctx).insertValueInto(skPredicate);
        substituted = outerVariableRenamer.rename(substituted);
        return substituted;
    }

    private ExtendFrontierResult computeRefinePredicate(List<BoolExpr> initialRegionSplitPredicates, List<BitVecExpr> arguments, ArrayList<Parameter> parameter, VariableRenamer outerVariableRenamer) {
        TimeTracer.start("computeRefinePred");
        SubstitutionArrays parameterSA = new SubstitutionArrays(arguments.size());
        for(int i = 0; i < arguments.size(); ++i) {
            parameterSA.from[i] = ctx.mkBVConst(parameter.get(i).name, DashConstants.BITS_IN_INT);
            parameterSA.to[i] = arguments.get(i);
        }

        BoolExpr refinePredicate = (BoolExpr)ctx.mkOr(initialRegionSplitPredicates.toArray(new BoolExpr[initialRegionSplitPredicates.size()]))
                .substitute(parameterSA.from, parameterSA.to);
        refinePredicate = outerVariableRenamer.reverseRename(refinePredicate);
        BoolExpr refinePredicateOptimized = Z3Helper.optimizeWithCtxSolverSimplify(refinePredicate, ctx);

        TimeTracer.end("computeRefinePred");
        if(refinePredicateOptimized.isTrue())
            throw new RuntimeException("Refine predicate cannot be true: " + refinePredicate);

        return new ExtendFrontierResult(refinePredicateOptimized);
    }

    private String computeUniquePrefix() {
        int num = uniqueNumber.incrementAndGet();
        return num + MAGIC_PREFIX_SEPARATOR;
    }

    public static class ExtendFrontierResult {

        public enum ResultType {
            TEST_INPUT,
            PREDICATE
        }

        public final ResultType resultType;
        public final BoolExpr predicate;
        public final Variables variables;
        public final ArrayList<Boolean> nonDetChoices;

        public ExtendFrontierResult(Variables variables, ArrayList<Boolean> nonDetChoices) {
            this.resultType = ResultType.TEST_INPUT;
            this.predicate = null;
            this.variables = variables;
            this.nonDetChoices = nonDetChoices;
        }

        public ExtendFrontierResult(BoolExpr predicate) {
            this.resultType = ResultType.PREDICATE;
            this.predicate = predicate;
            this.variables = null;
            this.nonDetChoices = null;
        }
    }
}
