package dk.au.cs.dash.util;

import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.cfg.GraphToDotConverter;
import dk.au.cs.dash.cfg.Region;
import dk.au.cs.dash.trace.RegionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.SootClass;
import soot.toolkits.graph.UnitGraph;
import soot.util.cfgcmd.CFGToDotGraph;
import soot.util.dot.DotGraph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Debug {
    private static final Logger log = LoggerFactory.getLogger(Debug.class);
    private static final boolean ENABLE_OUTPUT = log.isDebugEnabled();
    private static final boolean GRAPH_AFTER_EACH_REFINE = log.isTraceEnabled();
    private static final boolean OUTPUT_JIMPLE_FILES = log.isDebugEnabled();
    private static final AtomicInteger unique = new AtomicInteger(0);

    public static void toJimpleFile(SootClass clazz) {
        if (OUTPUT_JIMPLE_FILES)
            JimpleConverter.toJimpleFile(clazz);
    }

    public static void createDotOutput(Graph g) {
        createDotOutputInternal(g, ".graph", new ArrayList<Region>(), new ArrayList<RegionState>());
    }

    public static void createDotOutput(Graph g, int index) {
        if (GRAPH_AFTER_EACH_REFINE)
            createDotOutput(g, "" + index);
    }

    public static void createDotOutput(Graph g, int index, ArrayList<Region> path, ArrayList<RegionState> trace) {
        if (GRAPH_AFTER_EACH_REFINE)
        {
            createDotOutput(g, "" + index + ".path", path, new ArrayList<RegionState>());
            createDotOutput(g, "" + index + ".trace", path, trace);
        }
    }

    public static void createDotOutput(Graph g, String suffix) {
        if (GRAPH_AFTER_EACH_REFINE)
            createDotOutputInternal(g, ".graph." + suffix, new ArrayList<Region>(), new ArrayList<RegionState>());
    }

    public static void createDotOutput(Graph g, String suffix, ArrayList<Region> path, ArrayList<RegionState> trace) {
        if (GRAPH_AFTER_EACH_REFINE)
            createDotOutputInternal(g, ".graph." + suffix, path, trace);
    }

    private static void createDotOutputInternal(Graph g, String suffix, ArrayList<Region> path, ArrayList<RegionState> trace) {
        if (!ENABLE_OUTPUT)
            return;
        String dot = new GraphToDotConverter(g, path, trace).convert();
        createDotOutput(dot, g.className, unique.addAndGet(1) + g.methodName + suffix);
    }

    public static void createDotOutput(UnitGraph g) {
        if (!ENABLE_OUTPUT)
            return;
        try {
            Body b = g.getBody();
            DotGraph dotGraph = new CFGToDotGraph().drawCFG(g, b);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            dotGraph.render(out, 2);
            String dot = out.toString("UTF-8");
            createDotOutput(dot, getClassName(b), getMethodName(b));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createDotOutput(String dot, String className, String fileName) {
        if (!ENABLE_OUTPUT)
            return;
        GraphViz.toGraph(dot, className, fileName);
    }

    private static String getMethodName(Body b) {
        return b.getMethod().getName();
    }


    private static String getClassName(Body b) {
        return b.getMethod().getDeclaringClass().getName();
    }
}
