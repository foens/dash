package dk.au.cs.dash.cfg;

import com.microsoft.z3.BoolExpr;
import dk.au.cs.dash.cfg.Op.Op;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class Graph {
    public final Region entryPoint;
    public final Edges edges;
    public final String className;
    public final String methodName;
    public final Set<Region> errorRegions;
    public final List<Region> returnRegions;
    public final List<BoolExpr> initialRegionSplitPredicates;
    public boolean isMainProcedure = false;

    public Graph(Region entryPoint, Edges edges, String className, String methodName, Set<Region> errorRegions, List<Region> returnRegions) {
        this.entryPoint = requireNonNull(entryPoint);
        this.edges = requireNonNull(edges);
        this.className = requireNonNull(className);
        this.methodName = requireNonNull(methodName);
        this.errorRegions = requireNonNull(errorRegions);
        this.returnRegions = requireNonNull(returnRegions);
        this.initialRegionSplitPredicates = new ArrayList<>();
    }

    public Graph cloneWithoutStates() {
        Map<Region, Region> regionToCloned = new HashMap<>();
        for (Region region : this.edges.computeAllRegions()) {
            regionToCloned.put(region, createCopyWithoutStates(region));
        }
        Edges edges = new Edges();
        for (Edge edge : this.edges.computeAllEdges()) {
            edges.add(new Edge(edge.op, regionToCloned.get(edge.from), regionToCloned.get(edge.to)));
        }

        Region entryPoint = regionToCloned.get(this.entryPoint);

        Set<Region> errorRegions = new HashSet<>();
        for (Region errorRegion : this.errorRegions) {
            errorRegions.add(regionToCloned.get(errorRegion));
        }
        List<Region> returnRegions = new ArrayList<>();
        for (Region returnRegion : this.returnRegions) {
            returnRegions.add(regionToCloned.get(returnRegion));
        }
        Graph graph = new Graph(entryPoint, edges, className, methodName, errorRegions, returnRegions);
        graph.isMainProcedure = isMainProcedure;
        return graph;
    }

    private Region createCopyWithoutStates(Region region) {
        if(!region.predicate.isTrue())
            throw new RuntimeException("Graph should not have been changed before cloning it");
        return new Region(region.isErrorRegion, region.regionNumber, region.predicate);
    }

    public Op getOp(Region before, Region after) {
        return edges.get(before, after).op;
    }
}
