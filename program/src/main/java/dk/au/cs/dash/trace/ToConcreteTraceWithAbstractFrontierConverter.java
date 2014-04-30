package dk.au.cs.dash.trace;

import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.cfg.Region;
import dk.au.cs.dash.instrumentation.State;
import dk.au.cs.dash.util.DashAssert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class ToConcreteTraceWithAbstractFrontierConverter {
    private final AbstractErrorPath abstractErrorPath;
    private final Graph graph;

    public ToConcreteTraceWithAbstractFrontierConverter(AbstractErrorPath abstractErrorPath, Graph graph) {
        this.abstractErrorPath = abstractErrorPath;
        this.graph = graph;
    }

    public ConcreteTraceWithAbstractFrontier getOrderedAbstractTrace() {
        ArrayList<Region> errorTrace = abstractErrorPath.getErrorTrace();

        if(errorTrace.size() == 1) {
            return createSpecialTracePathOfLength1(errorTrace);
        }

        if(errorTrace.get(0).getStates().size() == 0) {
            return createSpecialTraceForInitial(errorTrace);
        }

        int beforeFrontierIndex = findRegionBeforeFrontier(errorTrace);

        Region beforeFrontier = errorTrace.get(beforeFrontierIndex);
        State frontierState = selectState(beforeFrontier);
        RegionState regionStateBeforeFrontier = new RegionState(beforeFrontier, frontierState);

        ArrayList<RegionState> trace = createTraceFromStartToFrontier(regionStateBeforeFrontier);

        Region afterFrontier = errorTrace.get(beforeFrontierIndex + 1);
        RegionState regionStateAfterFrontier = new RegionState(afterFrontier);
        trace.add(regionStateAfterFrontier);

        DashAssert.noConcreteRunsInAfterFrontier(afterFrontier);
        return new ConcreteTraceWithAbstractFrontier(trace, regionStateBeforeFrontier, regionStateAfterFrontier);
    }

    private ArrayList<RegionState> createTraceFromStartToFrontier(RegionState regionStateBeforeFrontier) {
        ArrayList<RegionState> trace = new ArrayList<>();
        trace.add(regionStateBeforeFrontier);

        State currentState = regionStateBeforeFrontier.state;
        Region currentRegion = regionStateBeforeFrontier.region;
        while (currentState.parent != null) {
            currentState = currentState.parent;
            currentRegion = findParentRegion(currentRegion, currentState);
            trace.add(new RegionState(currentRegion, currentState));
        }

        Collections.reverse(trace);
        return trace;
    }

    private Region findParentRegion(Region currentRegion, State parentState) {
        Set<Region> equivalenceRegions = graph.edges.getParentsOf(currentRegion);
        for (Region r : equivalenceRegions) {
            if (r.getStates().contains(parentState)) {
                return r;
            }
        }

        throw new IllegalStateException();
    }

    private ConcreteTraceWithAbstractFrontier createSpecialTraceForInitial(ArrayList<Region> errorTrace) {
        ArrayList<RegionState> trace = new ArrayList<>();
        RegionState region0 = new RegionState(errorTrace.get(0));
        RegionState region1 = new RegionState(errorTrace.get(1));
        trace.add(region0);
        trace.add(region1);
        return new ConcreteTraceWithAbstractFrontier(trace, region0, region1);
    }

    private ConcreteTraceWithAbstractFrontier createSpecialTracePathOfLength1(ArrayList<Region> errorTrace) {
        ArrayList<RegionState> trace = new ArrayList<>();
        RegionState region0 = new RegionState(errorTrace.get(0));
        trace.add(region0);

        Set<Region> afterFrontiers = graph.edges.getChildrenOf(errorTrace.get(0));
        if(afterFrontiers.size() != 1)
            throw new RuntimeException();
        RegionState region1 = new RegionState(afterFrontiers.iterator().next());
        trace.add(region1);

        return new ConcreteTraceWithAbstractFrontier(trace, region0, region1);
    }

    private int findRegionBeforeFrontier(ArrayList<Region> errorTrace) {
        for (int i = errorTrace.size() - 1; i >= 0; i--) {
            Region region = errorTrace.get(i);

            if (!region.isStateEmpty()) {
                return i;
            }
        }
        return 0;
    }

    private State selectState(Region region) {
        return region.getLastState();
    }
}
