package dk.au.cs.dash.test.dash.unittest.PathToTrace;

import com.microsoft.z3.Context;
import dk.au.cs.dash.PreparedMethod;
import dk.au.cs.dash.PreparedProgram;
import dk.au.cs.dash.ProgramPreparer;
import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.cfg.Region;
import dk.au.cs.dash.instrumentation.State;
import dk.au.cs.dash.instrumentation.Variables;
import dk.au.cs.dash.trace.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PathToTraceTest {

    private static Graph ifManyGraph;

    @BeforeClass
    public static void setup() {
        ProgramPreparer programPreparer = new ProgramPreparer(new Context());
        PreparedProgram preparedProgram = programPreparer.prepareProgram(IfManyExpressions.class.getName());
        PreparedMethod preparedMethod = preparedProgram.get(IfManyExpressions.class.getName(), "test");
        ifManyGraph = preparedMethod.g;
    }

    @Test
    public void findFirstEdgeIfNoStates() {
        Graph g = ifManyGraph.cloneWithoutStates();

        AbstractErrorPath pathToError = new AbstractErrorPathFinder(g).getPathToError();
        ConcreteTraceWithAbstractFrontier trace = new ToConcreteTraceWithAbstractFrontierConverter(pathToError, g).getOrderedAbstractTrace();

        ArrayList<RegionState> traceRegionList = trace.trace;
        Region beforeFrontierRegion = traceRegionList.get(0).region;
        assertEquals(beforeFrontierRegion, g.entryPoint);
        assertNull(traceRegionList.get(0).state);
        assertEquals(beforeFrontierRegion, trace.beforeFrontier.region);

        Region child = g.edges.getChildrenOf(beforeFrontierRegion).iterator().next();
        assertEquals(traceRegionList.get(1).region, child);
        assertNull(traceRegionList.get(1).state);
        assertEquals(child, trace.afterFrontier.region);

        assertEquals(traceRegionList.size(), 2);
    }

    @Test
    public void findTraceWithSingleConcreteState() {
        Graph g = ifManyGraph.cloneWithoutStates();
        g.entryPoint.addState(new State(null, new Variables(), 0, 0));

        AbstractErrorPath pathToError = new AbstractErrorPathFinder(g).getPathToError();
        ConcreteTraceWithAbstractFrontier trace = new ToConcreteTraceWithAbstractFrontierConverter(pathToError, g).getOrderedAbstractTrace();

        ArrayList<RegionState> traceRegionList = trace.trace;
        RegionState beforeFrontierRegionState = traceRegionList.get(0);
        assertEquals(beforeFrontierRegionState.region, g.entryPoint);
        assertEquals(beforeFrontierRegionState, trace.beforeFrontier);
        assertNotNull(beforeFrontierRegionState.state);

        Region child = g.edges.getChildrenOf(beforeFrontierRegionState.region).iterator().next();
        assertEquals(traceRegionList.get(1).region, child);
        assertNull(traceRegionList.get(1).state);
        assertEquals(traceRegionList.get(1), trace.afterFrontier);

        assertEquals(traceRegionList.size(), 2);
    }
}
