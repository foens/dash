package dk.au.cs.dash.trace;

import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.cfg.Region;
import dk.au.cs.dash.util.TimeTracer;

import java.util.*;

public class AbstractErrorPathFinder {
    private final Graph g;
    private final Set<Region> visited = new HashSet<>();
    private final Queue<Region> nextRegions = new ArrayDeque<>();
    private final Map<Region, Region> shortestWayBack = new HashMap<>();

    public AbstractErrorPathFinder(Graph g) {
        this.g = g;
    }

    public AbstractErrorPath getPathToError() {
        TimeTracer.start("FindAbstractErrorPath");
        AbstractErrorPath result = getPathToErrorTimed();
        TimeTracer.end("FindAbstractErrorPath");
        return result;
    }

    private AbstractErrorPath getPathToErrorTimed() {
        nextRegions.add(g.entryPoint);
        while (!nextRegions.isEmpty()) {
            Region r = nextRegions.remove();
            visit(r);
            if (r.isErrorRegion)
                return new AbstractErrorPath(backtrace(r));
        }
        return new AbstractErrorPath(new ArrayList<Region>());
    }

    private void visit(Region r) {
        if (visited.contains(r))
            return;
        visited.add(r);

        for (Region to : g.edges.getChildrenOf(r)) {
            if (!shortestWayBack.containsKey(to)) {
                nextRegions.add(to);
                shortestWayBack.put(to, r);
            }
        }
    }

    private ArrayList<Region> backtrace(Region r) {
        ArrayList<Region> trace = new ArrayList<>();
        while (r != null) {
            trace.add(r);
            r = shortestWayBack.get(r);
        }
        Collections.reverse(trace);
        return trace;
    }
}
