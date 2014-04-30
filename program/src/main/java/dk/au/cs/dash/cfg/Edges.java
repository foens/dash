package dk.au.cs.dash.cfg;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.*;

public class Edges {
    private final SetMultimap<Region, Region> forwardEdges = HashMultimap.create();
    private final SetMultimap<Region, Region> backwardEdges = HashMultimap.create();
    private final Map<Region, Map<Region, Edge>> regionToEdgeMap = new HashMap<>();

    public Set<Region> getParentsOf(Region r) {
        return Collections.unmodifiableSet(backwardEdges.get(r));
    }

    public Set<Region> getChildrenOf(Region r) {
        return Collections.unmodifiableSet(forwardEdges.get(r));
    }

    public void add(Edge e) {
        forwardEdges.put(e.from, e.to);
        backwardEdges.put(e.to, e.from);
        Map<Region, Edge> regionEdgeMap = regionToEdgeMap.get(e.from);
        if (regionEdgeMap == null)
            regionEdgeMap = new HashMap<>();
        else if (regionEdgeMap.containsKey(e.to))
            throw new IllegalStateException("Inserting edge that already existed");
        regionEdgeMap.put(e.to, e);
        regionToEdgeMap.put(e.from, regionEdgeMap);
    }

    public void remove(Edge e) {
        forwardEdges.remove(e.from, e.to);
        backwardEdges.remove(e.to, e.from);
        Map<Region, Edge> regionEdgeMap = regionToEdgeMap.get(e.from);
        if (regionEdgeMap == null || regionEdgeMap.remove(e.to) == null)
            throw new IllegalStateException("Removing non-existing edge");
    }

    public Edge get(Region from, Region to) {
        return regionToEdgeMap.get(from).get(to);
    }

    Set<Region> computeAllRegions() {
        Set<Region> regions = new TreeSet<>(new Comparator<Region>() {
            @Override
            public int compare(Region o1, Region o2) {
                return o1.hashCode() - o2.hashCode();
            }
        });
        regions.addAll(regionToEdgeMap.keySet());
        for (Region region : regionToEdgeMap.keySet()) {
            regions.addAll(regionToEdgeMap.get(region).keySet());
        }
        return regions;
    }

    Set<Edge> computeAllEdges() {
        Set<Edge> edges = new TreeSet<>(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.hashCode() - o2.hashCode();
            }
        });
        for (Region from : regionToEdgeMap.keySet())
            edges.addAll(regionToEdgeMap.get(from).values());
        return edges;
    }
}
