package dk.au.cs.dash.instrumentation;

public class State {
    public final State parent;
    public final Variables variables;
    public final int runId;
    public final int index;
    public State child;

    public State(State parent, Variables variables, int runId, int index) {
        this.parent = parent;
        this.variables = variables;
        this.runId = runId;
        this.index = index;
    }

    @Override
    public String toString() {
        return "#" + index + "[" + variables + ']';
    }
}
