package dk.au.cs.dash.cfg;

import com.microsoft.z3.BoolExpr;
import dk.au.cs.dash.cfg.Op.AssumeOp;
import dk.au.cs.dash.cfg.Op.Op;
import dk.au.cs.dash.instrumentation.State;
import dk.au.cs.dash.trace.RegionState;
import dk.au.cs.dash.util.Z3Printer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class GraphToDotConverter {
    private static final Logger log = LoggerFactory.getLogger(GraphToDotConverter.class);
    private final Graph graph;
    private final Set<Edge> errorPathEdges;
    private final Set<Edge> traceEdges;
    private final Edge frontierEdge;
    private final Map<Region, Integer> regionToIdMap = new HashMap<>();
    private final Set<Region> allRegions;
    private final Set<Region> unreachableRegions;
    private final Set<Edge> unreachableEdges;

    public GraphToDotConverter(Graph graph, ArrayList<Region> path, ArrayList<RegionState> trace) {
        this.graph = requireNonNull(graph);
        this.errorPathEdges = createErrorPathSet(graph, requireNonNull(path));
        this.traceEdges = createTraceEdgesSet(graph, requireNonNull(trace));
        frontierEdge = !trace.isEmpty() ? graph.edges.get(trace.get(trace.size()-2).region, trace.get(trace.size()-1).region) : null;
        allRegions = new LinkedHashSet<>();
        allRegions.add(graph.entryPoint);
        allRegions.addAll(graph.edges.computeAllRegions());
        unreachableRegions = findUnreachableRegions(graph);
        unreachableEdges = findUnreachableEdges(graph, unreachableRegions);
    }

    private static Set<Edge> findUnreachableEdges(Graph graph, Set<Region> unreachableRegions) {
        Set<Edge> unreachableEdges = new HashSet<>();
        for (Region parent : unreachableRegions) {
            for (Region child : graph.edges.getChildrenOf(parent)) {
                unreachableEdges.add(graph.edges.get(parent, child));
            }
        }
        return unreachableEdges;
    }

    private static Set<Edge> createErrorPathSet(Graph graph, ArrayList<Region> regions) {
        Set<Edge> errorPathEdges = new HashSet<>();
        for(int i = 0; i<regions.size()-1; i++)
            errorPathEdges.add(graph.edges.get(regions.get(i), regions.get(i + 1)));
        return errorPathEdges;
    }

    private static Set<Edge> createTraceEdgesSet(Graph graph, ArrayList<RegionState> regions) {
        Set<Edge> errorPathEdges = new HashSet<>();
        for(int i = 0; i<regions.size()-1; i++)
            errorPathEdges.add(graph.edges.get(regions.get(i).region, regions.get(i+1).region));
        return errorPathEdges;
    }

    private static Set<Region> findUnreachableRegions(Graph graph) {
        HashSet<Region> reachableRegions = new HashSet<>();
        findReachableRegionSet(graph.entryPoint, reachableRegions, graph);

        Set<Region> unreachableRegions = graph.edges.computeAllRegions();
        unreachableRegions.removeAll(reachableRegions);

        return unreachableRegions;
    }

    private static void findReachableRegionSet(Region region, Set<Region> visitedRegions, Graph graph) {
        visitedRegions.add(region);
        for (Region child : graph.edges.getChildrenOf(region))
            if (!visitedRegions.contains(child))
                findReachableRegionSet(child, visitedRegions, graph);
    }

    public String convert() {
        String dot = "digraph {\n";
        dot += "\tranksep=0.10\n";
        dot += "\tnodesep=0.12\n";
        dot += "\tnode[fontsize=10.5,shape=box,height=0.02,width=0.02,margin=\"0.05,0.05\"]\n";
        dot += "\tedge[fontsize=10.5,arrowsize=0.5]\n";
        dot += "\n";
        dot += addAllNodes();
        dot += "\n";
        dot += addNodeWithRefinePreds();
        dot += "\n";
        dot += addAllEdges(graph.edges.computeAllEdges());
        dot += "}";
        return dot;
    }

    private String addNodeWithRefinePreds() {
        List<BoolExpr> splits = graph.initialRegionSplitPredicates;
        if(splits.isEmpty() || graph.isMainProcedure)
            return "";

        StringBuilder b = new StringBuilder();
        boolean first = true;
        boolean lastPredWasAOneLiner = false;
        for (int i = 0; i < splits.size(); i++) {
            BoolExpr split = splits.get(i);
            if (!first)
                b.append("\\l");
            b.append(i);
            b.append(":\\l");
            String pred = Z3Printer.toString(split);
            b.append(pred);
            lastPredWasAOneLiner = !pred.contains("\\l");
            first = false;
        }
        b.append(lastPredWasAOneLiner ? "" : "\\l");
        return "splits [label=\"InitialRefines \\l" + b.toString() + "\",style=rounded]";
    }

    private String addAllNodes() {
        StringBuilder b = new StringBuilder();
        for (Region r : allRegions) {
            b.append("\t");
            b.append(addNode(r));
        }
        return b.toString();
    }

    private String addNode(Region r) {
        int id = getIdForRegion(r);
        String predicates = predicatesToString(r.predicate);
        boolean oneLiner = !predicates.contains("\\l");
        return id +
                " [label=\"" +
                r.regionNumber +
                addStateForRegionIfAny(r) +
                (predicates.isEmpty() ? "" : "\\l" + predicates + (oneLiner ? "" : "\\l")) +
                "\"" +
                addFillColor(r) +
                "]\n";
    }

    private String addStateForRegionIfAny(Region r) {
        if (r.isStateEmpty())
            return "";
        if (log.isTraceEnabled() && r.getStates().size() < 5)
            return " " + statesToString(r);
        return "    " + r.getStates().size() + "x";
    }

    private String statesToString(Region r) {
        StringBuilder b = new StringBuilder();
        b.append("{");
        for (State state : r.getStates()) {
            b.append(state.toString());
            b.append(", ");
        }
        b.delete(b.length() - 2, b.length());
        b.append("}");
        return b.toString();
    }

    private String predicatesToString(BoolExpr predicate) {
        if (predicate == null || predicate.isTrue())
            return "";
        return Z3Printer.toString(predicate);
    }

    private String addFillColor(Region r) {
        if (unreachableRegions.contains(r) && r.isErrorRegion)
            return ",style=filled,fillcolor=\"#F75D59\"";
        if (unreachableRegions.contains(r))
            return ",style=filled,fillcolor=yellow";
        if (r.isErrorRegion)
            return ",style=filled,fillcolor=red";
        if (graph.entryPoint == r)
            return ",style=filled,fillcolor=gray";
        return "";
    }

    private String addAllEdges(Set<Edge> edges) {
        StringBuilder b = new StringBuilder();
        for (Edge edge : edges) {
            b.append("\t");
            b.append(addEdge(edge));
        }
        return b.toString();
    }

    private String addEdge(Edge edge) {
        return getIdForRegion(edge.from) + "->" + getIdForRegion(edge.to) +
                "[label=\" " + edge.op.toString() + "\"" + getFontColorForOp(edge.op) + getEdgeStyle(edge) + "]\n";
    }

    private String getFontColorForOp(Op op) {
        if (op instanceof AssumeOp)
            return ",fontcolor=\"#0000FF\"";
        return "";
    }

    private String getEdgeStyle(Edge edge) {
        if(unreachableEdges.contains(edge))
        {
            if(traceEdges.contains(edge) || errorPathEdges.contains(edge) || frontierEdge == edge)
                throw new RuntimeException("Cannot be unreachable but be traversed");
            return ", color=yellow";
        }
        if(frontierEdge == edge)
            return ", color=\"#FF0000\", style=\"bold\"";
        if(traceEdges.contains(edge))
            return ", color=\"#00FF00\", style=\"bold\"";
        if(errorPathEdges.contains(edge))
            return ", color=\"#FF0000\", style=\"bold,dashed\"";
        return "";
    }

    private int getIdForRegion(Region r) {
        if (regionToIdMap.containsKey(r))
            return regionToIdMap.get(r);
        Integer newId = regionToIdMap.size();
        regionToIdMap.put(r, newId);
        return newId;
    }
}
