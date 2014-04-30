package dk.au.cs.dash.symbolic;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.cfg.Op.*;
import dk.au.cs.dash.cfg.Region;
import dk.au.cs.dash.instrumentation.State;
import dk.au.cs.dash.trace.ConcreteTraceWithAbstractFrontier;
import dk.au.cs.dash.util.SubstitutionArrays;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class RefinePredicate {
    private final Graph g;
    private final ConcreteTraceWithAbstractFrontier orderedTrace;
    private final Context ctx;

    public RefinePredicate(Graph g, ConcreteTraceWithAbstractFrontier orderedTrace, Context ctx) {
        this.ctx = requireNonNull(ctx);
        this.g = requireNonNull(g);
        this.orderedTrace = requireNonNull(orderedTrace);
    }

    public BoolExpr getPredicate() {
        Region sk = orderedTrace.afterFrontier.region;
        Op op = g.edges.get(orderedTrace.beforeFrontier.region, orderedTrace.afterFrontier.region).op;
        if (op instanceof AssumeOp) {
            return createPredicateAtIf(sk, (AssumeOp) op);
        } else if (op instanceof AssignOp) {
            AssignOp assignment = (AssignOp) op;
            return (BoolExpr) sk.predicate.substitute(assignment.name, assignment.rvalue);
        } else if (op instanceof ErrorOp) {
            return sk.predicate;
        } else if (op instanceof ReturnOp) {
            return sk.predicate;
        }

        throw new RuntimeException("GetPredicate: Need refine=" + op);
    }

    private BoolExpr createPredicateAtIf(Region sk, AssumeOp op) {
        Region skMinusOne = orderedTrace.beforeFrontier.region;
        if (orderedTrace.trace.size() > 2 && skPredicateDistributesStatesToNotPRegion(sk, skMinusOne))
            return sk.predicate;
        return ctx.mkAnd(sk.predicate, op.assumption);
    }

    private boolean skPredicateDistributesStatesToNotPRegion(Region sk, Region skMinusOne) {
        BoolExpr skMinusOnePredicate = ctx.mkNot(sk.predicate);
        BoolExpr skMinusOneCopyPredicate = sk.predicate;

        return predicateDistributesStatesToSkMinusOne(skMinusOne.getStates(), skMinusOnePredicate, skMinusOneCopyPredicate, ctx);
    }

    public static boolean predicateDistributesStatesToSkMinusOne(List<State> states, BoolExpr skMinusOnePredicate, BoolExpr skMinusOneCopyPredicate, Context ctx) {
        for (State state : states) {
            SubstitutionArrays sa = state.variables.getSubstitutionArrays(ctx);
            BoolExpr skMinusOnePredicateResult = (BoolExpr) skMinusOnePredicate.substitute(sa.from, sa.to).simplify();
            BoolExpr skMinusOneCopyPredicateResult = (BoolExpr) skMinusOneCopyPredicate.substitute(sa.from, sa.to).simplify();

            assertIsBoolConst(skMinusOnePredicateResult);
            assertIsBoolConst(skMinusOneCopyPredicateResult);

            if (!(skMinusOnePredicateResult.isTrue() && skMinusOneCopyPredicateResult.isFalse()))
                return false;
        }
        return true;
    }

    public static void assertIsBoolConst(BoolExpr boolExpr) {
        if (!boolExpr.isConst())
            throw new RuntimeException("Not a bool constant: " + boolExpr);
    }
}
