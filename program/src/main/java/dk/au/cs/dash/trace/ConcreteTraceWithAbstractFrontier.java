package dk.au.cs.dash.trace;

import java.util.ArrayList;

public class ConcreteTraceWithAbstractFrontier {
    public final ArrayList<RegionState> trace;
    public final RegionState beforeFrontier;
    public final RegionState afterFrontier;

    public ConcreteTraceWithAbstractFrontier(ArrayList<RegionState> trace, RegionState beforeFrontier, RegionState afterFrontier) {
        if(trace.size() == 1)
            throw new RuntimeException("Does not make sense");
        this.trace = trace;
        this.beforeFrontier = beforeFrontier;
        this.afterFrontier = afterFrontier;
    }

    @Override
    public String toString() {
        return "ConcreteTraceWithAbstractFrontier{\n" +
                "trace=" + trace +
                "\n, beforeFrontier=" + beforeFrontier +
                "\n, afterFrontier=" + afterFrontier + '}';
    }
}
