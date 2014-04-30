package dk.au.cs.dash;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.cfg.Edge;
import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.cfg.Op.ReturnOp;
import dk.au.cs.dash.cfg.Region;
import dk.au.cs.dash.instrumentation.TestRunner;
import dk.au.cs.dash.instrumentation.State;
import dk.au.cs.dash.instrumentation.Variables;
import dk.au.cs.dash.symbolic.ExtendFrontier;
import dk.au.cs.dash.symbolic.RefinePredicate;
import dk.au.cs.dash.symbolic.SATSolver;
import dk.au.cs.dash.symbolic.VariableInserter;
import dk.au.cs.dash.trace.AbstractErrorPath;
import dk.au.cs.dash.trace.AbstractErrorPathFinder;
import dk.au.cs.dash.trace.ConcreteTraceWithAbstractFrontier;
import dk.au.cs.dash.trace.ToConcreteTraceWithAbstractFrontierConverter;
import dk.au.cs.dash.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static dk.au.cs.dash.util.DashConstants.createResultVariable;
import static java.util.Objects.requireNonNull;

public class Dash {
    private static final int MAX_NUMBER_OF_ITERATIONS = 80;
    private static final Logger logger = LoggerFactory.getLogger(Dash.class);

    private final PreparedProgram preparedProgram;
    private final Context ctx;

    //Methods used by a method under test
    public static void error() {}
    public static boolean nondet() { throw new RuntimeException("Should never actually be invoked"); }

    public Dash(PreparedProgram preparedProgram, Context context) {
        this.preparedProgram = requireNonNull(preparedProgram);
        ctx = requireNonNull(context);
    }

    public DashResult dashLoop(String className, String methodName, BoolExpr extraInputConstraints, BoolExpr extraExitConstraints) {
        logger.info("dashLoop called on " + className + "." + methodName);

        PreparedMethod preparedMethod = preparedProgram.get(className, methodName);
        Graph g = preparedMethod.g.cloneWithoutStates();
        TestRunner testRunner = new TestRunner(ctx, preparedProgram, preparedMethod.parameters, className, methodName, g);
        addInputAndExitConstraintsToGraph(g, extraInputConstraints, extraExitConstraints);
        DashAssert.CheckUniquenessOfInputToMethod checkUniquenessOfInputToMethod = new DashAssert.CheckUniquenessOfInputToMethod();

        Debug.createDotOutput(g, "0.init");
        for (int i = 0; i < MAX_NUMBER_OF_ITERATIONS; ++i) {
            logger.info("Starting iteration " + i);

            AbstractErrorPath path = findAbstractErrorPath(g);
            if (path.isEmpty()) {
                return new DashResult(g);
            }

            ConcreteTraceWithAbstractFrontier trace = convertToConcreteTraceWithAbstractFrontier(path, g);
            Debug.createDotOutput(g, i, path.getErrorTrace(), trace.trace);
            ExtendFrontier.ExtendFrontierResult extendFrontierResult = extendFrontier(trace, preparedMethod, g);

            if (extendFrontierResult.resultType == ExtendFrontier.ExtendFrontierResult.ResultType.TEST_INPUT) {
                runTest(checkUniquenessOfInputToMethod, testRunner, extendFrontierResult.variables, extendFrontierResult.nonDetChoices, i);
                boolean errorWasReached = isErrorRegionReached(g);
                if (errorWasReached) {
                    Debug.createDotOutput(g, i + ".error");
                    return new DashResult(extendFrontierResult.variables, extendFrontierResult.nonDetChoices);
                }
            } else {
                BoolExpr predicate = extendFrontierResult.predicate;
                refineGraph(g, trace, predicate);
            }
            Debug.createDotOutput(g, i);

            /*for (PreparedMethod method : preparedProgram.getGraphs()) {
                Debug.createDotOutput(method.g, i);
            }*/
        }
        throw new RuntimeException("Took more then " + MAX_NUMBER_OF_ITERATIONS + " iterations of DashLoop");
    }

    private ExtendFrontier.ExtendFrontierResult extendFrontier(ConcreteTraceWithAbstractFrontier orderedTrace, PreparedMethod preparedMethod, Graph g) {
        return new ExtendFrontier(orderedTrace, preparedMethod, g, this, ctx, preparedProgram).getTraceOrPredicate();
    }

    private AbstractErrorPath findAbstractErrorPath(Graph g) {
        AbstractErrorPath pathToError = new AbstractErrorPathFinder(g).getPathToError();
        if(logger.isTraceEnabled())
            if(pathToError.isEmpty())
                logger.info("It was proven that no errors existed");
            else
                logger.trace("AbstractErrorPath was found=" + pathToError);
        return pathToError;
    }

    private ConcreteTraceWithAbstractFrontier convertToConcreteTraceWithAbstractFrontier(AbstractErrorPath path, Graph g) {
        TimeTracer.start("ToConcreteTraceWithAbstractFrontierConverter");
        ConcreteTraceWithAbstractFrontier trace = new ToConcreteTraceWithAbstractFrontierConverter(path, g).getOrderedAbstractTrace();
        TimeTracer.end("ToConcreteTraceWithAbstractFrontierConverter");
        if(logger.isTraceEnabled())
            logger.trace("ConcreteTraceWithAbstractFrontier=" + trace);
        return trace;
    }

    private void refineGraph(Graph g, ConcreteTraceWithAbstractFrontier orderedTrace, BoolExpr p) {
        TimeTracer.start("refineGraph");
        if(logger.isTraceEnabled()) {
            logger.trace("New predicate=" + p);
        }

        if(p.isTrue())
            throw new RuntimeException("Refinement predicate cannot be true!");

        Region skMinusOne = orderedTrace.beforeFrontier.region;
        Region sk = orderedTrace.afterFrontier.region;

        if(g.entryPoint == skMinusOne) {
            // We are splitting the initial region.
            // Simply remove the frontier edge out of the region
            g.edges.remove(g.edges.get(skMinusOne, sk));
            g.initialRegionSplitPredicates.add(p);
            logger.warn("Removed edge from intial region");

            //Will always be unsat, uncomment to assert it
            //if(checkIsSat(ctx.mkAnd(p, skMinusOne.predicate)))
            //    throw new RuntimeException();
        } else {
            DashAssert.assertRefinePredMakesMakesADifference(p, skMinusOne, ctx);

            // Make clone of skMinusOne
            Region skMinusOneCopy = new Region(skMinusOne.isErrorRegion, skMinusOne.regionNumber, skMinusOne.predicate);

            // Add new predicates
            BoolExpr predAndNotP = ctx.mkAnd(ctx.mkNot(p), skMinusOne.predicate);
            BoolExpr predAndP = ctx.mkAnd(p, skMinusOneCopy.predicate);

            BoolExpr predAndNotPOpt = optimizePredicateInsideRefineGraph(predAndNotP, false);
            BoolExpr predAndPOpt = optimizePredicateInsideRefineGraph(predAndP, true);


            if(predAndNotPOpt.isFalse()) {
                throw new RuntimeException("skMinusOne cannot end up with false");
            }

            skMinusOne.predicate = predAndNotPOpt;
            skMinusOneCopy.predicate = predAndPOpt;

            //DashAssert.allStatesStayInSkMinusOne(skMinusOne, skMinusOneCopy, ctx);
            List<State> states = new ArrayList<>(skMinusOne.getStates());
            skMinusOne.clearStates();
            for (State state : states) {
                VariableInserter variableInserter = new VariableInserter(state.variables.getSubstitutionArrays(ctx));
                BoolExpr skMinusOnePredicate = (BoolExpr) variableInserter.insertValueInto(skMinusOne.predicate).simplify();
                BoolExpr skMinusOneCopyPredicate = (BoolExpr) variableInserter.insertValueInto(skMinusOneCopy.predicate).simplify();
                RefinePredicate.assertIsBoolConst(skMinusOnePredicate);
                RefinePredicate.assertIsBoolConst(skMinusOneCopyPredicate);
                if(skMinusOnePredicate.isTrue() && skMinusOneCopyPredicate.isFalse())
                    skMinusOne.addState(state);
                else if(skMinusOnePredicate.isFalse() && skMinusOneCopyPredicate.isTrue())
                    skMinusOneCopy.addState(state); // Not reachable at any test (when writing this)
                else
                    throw new RuntimeException();
            }

            // Add all outgoing edges to copy
            for (Region child : g.edges.getChildrenOf(skMinusOne)) {
                Edge e = g.edges.get(skMinusOne, child);
                Edge newE = new Edge(e.op, skMinusOneCopy, child);
                g.edges.add(newE);
            }

            if(!skMinusOneCopy.predicate.isFalse()) {
                // Add all ingoing edges to copy
                for (Region parent : g.edges.getParentsOf(skMinusOne)) {
                    Edge e = g.edges.get(parent, skMinusOne);
                    Edge newE = new Edge(e.op, parent, skMinusOneCopy);
                    g.edges.add(newE);
                }
            }

            // Remove edge from skMinusOne to sk
            g.edges.remove(g.edges.get(skMinusOne, sk));
        }
        TimeTracer.end("refineGraph");
    }

    private void addInputAndExitConstraintsToGraph(Graph g, final BoolExpr inputConstraint, final BoolExpr exitConstraint) {
        if(inputConstraint != null) {
            if(inputConstraint.isFalse())
                throw new RuntimeException("Input constraints cannot be false");

            Region entryRegion = g.entryPoint;
            if(!entryRegion.predicate.isTrue())
                throw new RuntimeException("Cannot add input constraints on a region that already contains constraints");
            entryRegion.predicate = optimizePredicateOutsideRefineGraph(inputConstraint, true);
            if(entryRegion.predicate.isFalse())
                throw new RuntimeException("Input constraints cannot be false, even after simplification");
        }

        if(exitConstraint != null) {
            for (Region returnRegion : g.returnRegions) {
                if(!returnRegion.predicate.isTrue())
                    throw new RuntimeException("Cannot add exit constraints on a region that already contains constraints");

                Set<Region> parentsOf = g.edges.getParentsOf(returnRegion);
                if(parentsOf.size() != 1)
                    throw new RuntimeException("Not singular return statement, count=" + parentsOf.size());

                Region parentRegion = parentsOf.iterator().next();
                Edge returnEdge = g.edges.get(parentRegion, returnRegion);
                ReturnOp returnOp = (ReturnOp) returnEdge.op;

                BoolExpr exitConstraintForReturnOp = createExitConstraintForReturnOp(exitConstraint, returnOp);
                returnRegion.predicate = optimizePredicateOutsideRefineGraph(ctx.mkNot(exitConstraintForReturnOp), true);

                BoolExpr errorRegionPredicate = optimizePredicateOutsideRefineGraph(exitConstraintForReturnOp, true);
                Region errorRegion = new Region(true, returnRegion.regionNumber, errorRegionPredicate);
                g.errorRegions.add(errorRegion);
                g.edges.add(new Edge(returnOp, parentRegion, errorRegion));
            }
        }
    }

    private BoolExpr createExitConstraintForReturnOp(BoolExpr exitConstraint, ReturnOp returnOp) {
        if (returnOp.getReturnVariable() == null) {
            return exitConstraint;
        }
        return (BoolExpr)exitConstraint.substitute(createResultVariable(ctx), returnOp.getReturnVariable());
    }

    private BoolExpr optimizePredicateInsideRefineGraph(BoolExpr predicate, boolean couldBeUnsatisfiable) {
        TimeTracer.start("refineGraph.ctx-solver-simplify");
        BoolExpr result = Z3Helper.optimizeWithCtxSolverSimplify(predicate, ctx);
        TimeTracer.end("refineGraph.ctx-solver-simplify");
        DashAssert.assertCtxSolverSimplifyIsGoodEnough(predicate, couldBeUnsatisfiable, result, ctx);
        return result;
    }

    private BoolExpr optimizePredicateOutsideRefineGraph(BoolExpr predicate, boolean couldBeUnsatisfiable) {
        TimeTracer.start("ctx-solver-simplify");
        BoolExpr result = Z3Helper.optimizeWithCtxSolverSimplify(predicate, ctx);
        TimeTracer.end("ctx-solver-simplify");
        DashAssert.assertCtxSolverSimplifyIsGoodEnough(predicate, couldBeUnsatisfiable, result, ctx);
        return result;
    }

    private void runTest(DashAssert.CheckUniquenessOfInputToMethod asserter, TestRunner testRunner, Variables variables, ArrayList<Boolean> nonDetChoices, int runId) {
        asserter.check(variables, nonDetChoices);
        TimeTracer.start("runTest");
        testRunner.createAndCallTest(variables, nonDetChoices, runId);
        TimeTracer.end("runTest");
    }

    private static boolean isErrorRegionReached(Graph g) {
        TimeTracer.start("isErrorRegionReached");
        for (Region r : g.errorRegions) {
            if (!r.isStateEmpty()) {
                TimeTracer.end("isErrorRegionReached");
                return true;
            }
        }
        TimeTracer.end("isErrorRegionReached");
        return false;
    }
}
