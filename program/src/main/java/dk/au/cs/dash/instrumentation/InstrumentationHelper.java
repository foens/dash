package dk.au.cs.dash.instrumentation;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import dk.au.cs.dash.PreparedProgram;
import dk.au.cs.dash.cfg.Edge;
import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.cfg.Op.*;
import dk.au.cs.dash.cfg.Region;
import dk.au.cs.dash.util.SubstitutionArrays;

import java.util.*;

public class InstrumentationHelper {
    private final Context ctx;
    private final PreparedProgram preparedProgram;
    private final int runId;
    private final Queue<Boolean> nondetChoices;
    private MethodState methodState;
    private final Queue<MethodState> methodStates = Collections.asLifoQueue(new LinkedList<MethodState>());
    private int nextIndex = 0;

    public InstrumentationHelper(PreparedProgram preparedProgram, Variables externalVariableMap, ArrayList<Boolean> nonDetChoices, Context ctx, int runId, Graph g) {
        this.preparedProgram = preparedProgram;
        this.runId = runId;
        this.ctx = ctx;
        this.nondetChoices = new LinkedList<>(nonDetChoices);

        pushNewMethodState(g);
        methodState.variables = externalVariableMap.createCopy();
    }

    public boolean nondet() {
        if(nondetChoices.isEmpty()) {
            return false;
        } else {
            return nondetChoices.remove();
        }
    }

    @SuppressWarnings("unused")
    public void assign(String variableName, Object value) {
        assertStatementInstanceOf(AssignOp.class);
        assignWithoutAssertCheck(variableName, value);
    }

    private void assignWithoutAssertCheck(String variableName, Object value) {
        if(value instanceof Integer)
            methodState.variables.put(variableName, (Integer) value);
        else
            throw new RuntimeException(variableName + " = " + value);
        advanceToNextRegionAndSaveState();
    }

    @SuppressWarnings("unused")
    public void assign(String variableName, int value) {
        assign(variableName, (Integer) value);
    }

    @SuppressWarnings("unused")
    public void assign(String variableName, byte value) {
        assign(variableName, (int) value);
    }

    @SuppressWarnings("unused")
    public void assign(String variableName, short value) {
        assign(variableName, (int) value);
    }

    @SuppressWarnings("unused")
    public void assign(String variableName, boolean value) {
        assign(variableName, value ? 1 : 0);
    }


    private void loadParam(String variableName, Object value) {
        if(value instanceof Integer)
            methodState.variables.put(variableName, (Integer) value);
        else
            throw new RuntimeException(variableName + " = " + value);
    }

    @SuppressWarnings("unused")
    public void loadParam(String variableName, int value) {
        loadParam(variableName, (Integer) value);
    }

    @SuppressWarnings("unused")
    public void loadParam(String variableName, byte value) {
        loadParam(variableName, (int) value);
    }

    @SuppressWarnings("unused")
    public void loadParam(String variableName, short value) {
        loadParam(variableName, (int) value);
    }

    @SuppressWarnings("unused")
    public void loadParam(String variableName, boolean value) {
        loadParam(variableName, value ? 1 : 0);
    }

    @SuppressWarnings("unused")
    public void loadParamFinished() {
        methodState.previousRegion = methodState.graph.entryPoint;
        saveState();
    }

    @SuppressWarnings("unused")
    public void tick(String opName) {
        assertStatementName(opName);
        advanceToNextRegionAndSaveState();
    }


    @SuppressWarnings("unused")
    public void callStatement() {
        assertStatementInstanceOf(CallOp.class);
        CallOp callOp = (CallOp) getFirstOp();
        pushNewMethodState(callOp);
    }

    @SuppressWarnings("unused")
    public void callStatementEnd() {
        popMethodState();
        advanceToNextRegionAndSaveState();
    }


    @SuppressWarnings("unused")
    public void assignWithCall() {
        assertStatementInstanceOf(AssignWithCallOp.class);
        AssignWithCallOp assignWithCallOp = (AssignWithCallOp) getFirstOp();
        CallOp callOp = assignWithCallOp.callOp;
        pushNewMethodState(callOp);
    }

    @SuppressWarnings("unused")
    public void assignWithCallEnd(String variableName, Object value) {
        popMethodState();
        assignWithoutAssertCheck(variableName, value);
    }

    @SuppressWarnings("unused")
    public void assignWithCallEnd(String variableName, int value) {
        assignWithCallEnd(variableName, (Integer) value);
    }

    @SuppressWarnings("unused")
    public void assignWithCallEnd(String variableName, boolean value) {
        assignWithCallEnd(variableName, value ? 1 : 0);
    }

    private void pushNewMethodState(CallOp callOp) {
        Graph m = preparedProgram.get(callOp.className, callOp.methodName).g;
        pushNewMethodState(m);
    }

    private void pushNewMethodState(Graph m) {
        MethodState newMethodState = new MethodState();
        newMethodState.graph = m;
        newMethodState.variables = new Variables();
        methodStates.add(methodState);
        methodState = newMethodState;
    }

    private void popMethodState() {
        methodState = methodStates.remove();
    }

    private void advanceToNextRegionAndSaveState() {
        advanceToNextRegion();
        saveState();
    }

    private void saveState() {
        State current = new State(methodState.previousState, methodState.variables.createCopy(), runId, nextIndex++);
        methodState.previousRegion.addState(current);

        if(methodState.previousState != null)
            methodState.previousState.child = current;

        methodState.previousState = current;
    }

    private void advanceToNextRegion() {
        List<Region> nextRegions = new ArrayList<>();
        for (Region possibleRegion : methodState.graph.edges.getChildrenOf(methodState.previousRegion)) {
            Edge edge = methodState.graph.edges.get(methodState.previousRegion, possibleRegion);

            if (isPossibleTransition(edge, methodState.variables))
                nextRegions.add(possibleRegion);
        }
        if (nextRegions.size() != 1)
            throw new RuntimeException("Not possible to find next region. We found " + nextRegions.size() + " matches");
        methodState.previousRegion = nextRegions.get(0);
    }

    private boolean isPossibleTransition(Edge edge, Variables variables) {
        Op op = edge.op;

        if(op instanceof FakeAssumeTrueOp) {
            op = ((FakeAssumeTrueOp) op).getRealOp();
        }

        boolean isRegionPredicateTrue = evaluatesToTrue(edge.to.predicate, variables);
        if(op instanceof AssumeOp)
            return evaluatesToTrue(((AssumeOp) op).assumption, variables) && isRegionPredicateTrue;
        return isRegionPredicateTrue;
    }

    private boolean evaluatesToTrue(BoolExpr exp, Variables v) {
        if(exp == null)
            return true;

        SubstitutionArrays sa = v.getSubstitutionArrays(ctx);
        Expr result = exp.substitute(sa.from, sa.to).simplify();

        if(result.isTrue())
            return true;
        else if(result.isFalse())
            return false;
        else
            throw new RuntimeException("Could not complete evaluation: " + result);
    }

    private Op getFirstOp()
    {
        return methodState.graph.edges.get(methodState.previousRegion, methodState.graph.edges.getChildrenOf(methodState.previousRegion).iterator().next()).op;
    }

    private <T extends Op> void assertStatementInstanceOf(Class<T> opClass) {
        for (Region region : methodState.graph.edges.getChildrenOf(methodState.previousRegion)) {
            Op op = methodState.graph.edges.get(methodState.previousRegion, region).op;
            if(op instanceof FakeAssumeTrueOp)
                op = ((FakeAssumeTrueOp) op).getRealOp();
            if (!opClass.isInstance(op))
                throw new IllegalStateException("Expected " + opClass.getSimpleName() + " but got " + op.getClass().getSimpleName());
        }
    }

    private <T extends Op> void assertStatementName(String opName) {
        for (Region region : methodState.graph.edges.getChildrenOf(methodState.previousRegion)) {
            Op actualOp = methodState.graph.edges.get(methodState.previousRegion, region).op;
            if(actualOp instanceof FakeAssumeTrueOp)
                actualOp = ((FakeAssumeTrueOp) actualOp).getRealOp();
            if (!opName.equals(actualOp.getClass().getSimpleName()))
                throw new IllegalStateException("Expected " + opName + " but got " + actualOp.getClass().getSimpleName());
        }
    }

    private static class MethodState {
        public Graph graph;
        public Region previousRegion;
        public State previousState = null;
        public Variables variables = new Variables();
    }
}
