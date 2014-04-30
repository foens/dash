package dk.au.cs.dash.cfg;

import com.microsoft.z3.BoolExpr;
import dk.au.cs.dash.instrumentation.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Region {
    public final int regionNumber;
    public final boolean isErrorRegion;
    public BoolExpr predicate;
    private final ArrayList<State> concreteStates = new ArrayList<>();

    public Region(boolean isErrorRegion, int regionNumber, BoolExpr predicate) {
        this.isErrorRegion = isErrorRegion;
        this.regionNumber = regionNumber;
        this.predicate = predicate;
    }

    @Override
    public String toString() {
        if (isErrorRegion)
            return "ErrorRegion=" + regionNumber;
        else
            return "Region=" + regionNumber;
    }

    public boolean isStateEmpty() {
        return concreteStates.isEmpty();
    }

    public void clearStates() {
        concreteStates.clear();
    }

    public void addState(State state) {
        concreteStates.add(state);
    }

    public List<State> getStates() {
        return Collections.unmodifiableList(concreteStates);
    }

    public State getLastState() {
        return concreteStates.get(concreteStates.size() - 1);
    }
}
