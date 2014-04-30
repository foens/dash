package dk.au.cs.dash.test.z3;

import com.microsoft.z3.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Z3Test {
    @Test
    public void test() {
        try {
            Context ctx = new Context();

            IntExpr x = ctx.mkIntConst("x");
            BoolExpr boolExpr = ctx.mkLt(x, ctx.mkInt(3));

            Solver s = ctx.mkSolver();
            s.add(boolExpr);

            Status status = s.check();
            System.out.println(status);
            if (status == Status.SATISFIABLE)
                System.out.println(s.getModel());

            ctx.dispose();
        } catch (Z3Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSimplify() {
        try {
            Context ctx = new Context();

            BoolExpr z3 = ctx.mkBoolConst("z3");
            BoolExpr z2 = ctx.mkBoolConst("z2");

            BoolExpr boolExpr = ctx.mkAnd(ctx.mkOr(ctx.mkEq(z3, ctx.mkBool(false)),
                    ctx.mkNot(ctx.mkEq(z2, ctx.mkBool(false)))),
                    ctx.mkOr(ctx.mkEq(z3, ctx.mkBool(false)),
                            ctx.mkEq(z2, ctx.mkBool(false))));

            System.out.println(boolExpr);
            System.out.println(boolExpr.simplify());
            System.out.println("====================");


            Solver s = ctx.mkSolver();
            s.add(boolExpr);
            Status status = s.check();
            System.out.println(status);
            if (status == Status.SATISFIABLE)
                System.out.println(s.getModel());

            ctx.dispose();
        } catch (Z3Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSimplifyOfNot() {
        try {
            Context ctx = new Context();

            BitVecExpr z1 = ctx.mkBVConst("z1", 32);
            BitVecExpr z2 = ctx.mkBVConst("z2", 32);

            BoolExpr boolExpr = ctx.mkNot(ctx.mkBVSLE(z1, z2));

            System.out.println(boolExpr);
            Params params = ctx.mkParams();
            params.add("local-ctx", true);
            System.out.println(boolExpr.simplify(params));
            System.out.println("====================");


            Solver s = ctx.mkSolver();
            s.add(boolExpr);
            Status status = s.check();
            System.out.println(status);
            if (status == Status.SATISFIABLE)
                System.out.println(s.getModel());

            ctx.dispose();
        } catch (Z3Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSimplifyOfArithmetic() {
        try {
            Context ctx = new Context();

            BitVecExpr i = ctx.mkBVConst("i", 32);

            BoolExpr boolExpr = ctx.mkAnd(ctx.mkNot(ctx.mkBVSLE(ctx.mkBV(1, 32), ctx.mkBVAdd(ctx.mkBV(1, 32), i))), ctx.mkEq(i, ctx.mkBV(-1, 32)));
            Tactic tactic = ctx.mkTactic("ctx-solver-simplify");
            Goal g = ctx.mkGoal(true, true, false);
            g.add(boolExpr);
            ApplyResult apply = tactic.apply(g);

            Goal goal = apply.getSubgoals()[0];
            BoolExpr[] formulas = goal.getFormulas();


            System.out.println(boolExpr);
            System.out.println(formulas[0]);
            System.out.println(boolExpr.simplify());
            System.out.println("====================");


            Solver s = ctx.mkSolver();
            s.add(boolExpr);
            Status status = s.check();
            System.out.println(status);
            if (status == Status.SATISFIABLE)
                System.out.println(s.getModel());

            ctx.dispose();
        } catch (Z3Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBug() {
        try {
            Context ctx = new Context();
            BitVecExpr p0 = ctx.mkBVConst("p0", 32);
            BitVecExpr x = ctx.mkBVConst("x", 32);
            BitVecNum three = ctx.mkBV(3, 32);
            BitVecNum thousand = ctx.mkBV(1000, 32);
            BoolExpr p0NotLessThanThree = ctx.mkNot(ctx.mkBVSLE(p0, three));
            BoolExpr thousandNotLessThanP0 = ctx.mkNot(ctx.mkBVSLE(thousand, p0));

            BoolExpr original = ctx.mkAnd(
                    ctx.mkOr(
                            ctx.mkAnd(p0NotLessThanThree, thousandNotLessThanP0, ctx.mkBVSLE(x, ctx.mkBV(2, 32))),
                            ctx.mkAnd(p0NotLessThanThree, thousandNotLessThanP0, ctx.mkNot(ctx.mkEq(x, ctx.mkBVAdd(ctx.mkBV(1, 32), p0))))
                    ),
                    p0NotLessThanThree
            );
            Tactic tactic = ctx.mkTactic("ctx-solver-simplify");
            Goal g = ctx.mkGoal(true, true, false);
            g.add(original);
            ApplyResult apply = tactic.apply(g);
            assertEquals(1, apply.getSubgoals().length);
            Goal goal = apply.getSubgoals()[0];

            System.out.println("==================== expression");
            System.out.println(original);
            System.out.println("==================== ctx-solver-simplify");
            System.out.println(goal);

            Expr[] from = {p0, x};
            Expr[] to = {ctx.mkBV(1, 32), ctx.mkBV(10, 32)};

            BoolExpr simplified = ctx.mkAnd(goal.getFormulas());
            Expr originalEvaluated = original.substitute(from, to).simplify();
            Expr simplifiedEvaluated = simplified.substitute(from, to).simplify();

            System.out.println("==================== Original evaluated");
            System.out.println(originalEvaluated);
            System.out.println("==================== ctx-solver-simplify evaluated");
            System.out.println(simplifiedEvaluated);
            System.out.println("====================");

            // These should really be the same...
            assertTrue(originalEvaluated.isFalse());
            assertTrue("Simplified version does not evaluate to false!", simplifiedEvaluated.isFalse());

            ctx.dispose();
        } catch (Z3Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testArray() {
        Context ctx = new Context();

        ArrayExpr t = ctx.mkArrayConst("t", ctx.mkBitVecSort(32), ctx.mkBitVecSort(32));

        Solver s = ctx.mkSolver();
        BitVecExpr t0= (BitVecExpr) ctx.mkSelect(t, ctx.mkBV(0, 32));
        s.add(ctx.mkEq(ctx.mkBV(0, 32), ctx.mkBVAND(t0, ctx.mkBV(0, 32))));
        s.add(ctx.mkEq(ctx.mkBV(1, 32), ctx.mkBVAND(t0, ctx.mkBV(1, 32))));
        s.add(ctx.mkEq(ctx.mkBV(0, 32), ctx.mkBVAND(t0, ctx.mkBV(2, 32))));
        s.add(ctx.mkEq(ctx.mkBV(4, 32), ctx.mkBVAND(t0, ctx.mkBV(4, 32))));
        s.add(ctx.mkEq(ctx.mkBV(0, 32), ctx.mkBVAND(t0, ctx.mkBV(8, 32))));
        s.add(ctx.mkEq(ctx.mkBV(16, 32), ctx.mkBVAND(t0, ctx.mkBV(16, 32))));
        s.add(ctx.mkEq(ctx.mkBV(0, 32), ctx.mkBVAND(t0, ctx.mkBV(32, 32))));
        s.add(ctx.mkEq(ctx.mkBV(64, 32), ctx.mkBVAND(t0, ctx.mkBV(64, 32))));



        BitVecExpr t3= (BitVecExpr) ctx.mkSelect(t, ctx.mkBV(3, 32));
        s.add(ctx.mkEq(ctx.mkBV(0xAA55AA55, 32), t3));

        //TODO how to encode length?

        Status status = s.check();
        System.out.println(status);
        if (status == Status.SATISFIABLE)
            System.out.println(s.getModel());

        ctx.dispose();
    }


    @Test
    public void testH() {
        Context ctx = new Context();

        ArrayExpr h = ctx.mkArrayConst("h", ctx.mkBitVecSort(32), ctx.mkBitVecSort(32));

        Solver s = ctx.mkSolver();
        BitVecNum xp = ctx.mkBV(1, 32);
        s.add(ctx.mkEq(ctx.mkSelect(h, xp), ctx.mkBV(3, 32)));
        s.add(ctx.mkEq(ctx.mkSelect(h, ctx.mkBVAdd(xp, ctx.mkBV(1, 32))), ctx.mkBV(0xAA, 32)));
        s.add(ctx.mkEq(ctx.mkSelect(h, ctx.mkSelect(h, xp)), ctx.mkBV(0, 32)));
        s.add(ctx.mkEq(ctx.mkSelect(h, ctx.mkBVAdd((BitVecExpr)ctx.mkSelect(h, xp), ctx.mkBV(1, 32))), ctx.mkBV(0x55, 32)));

        Status status = s.check();
        System.out.println(status);
        if (status == Status.SATISFIABLE)
            System.out.println(s.getModel());

        ctx.dispose();
    }

    @Test
    public void testHeapUpdate() {
        Context ctx = new Context();

        ArrayExpr h = ctx.mkArrayConst("h", ctx.mkBitVecSort(32), ctx.mkBitVecSort(32));

        Solver s = ctx.mkSolver();
        BitVecNum xp = ctx.mkBV(1, 32);
        s.add(ctx.mkEq(ctx.mkSelect(h, xp), ctx.mkBV(3, 32)));
        s.add(ctx.mkEq(ctx.mkSelect(h, ctx.mkBVAdd(xp, ctx.mkBV(1, 32))), ctx.mkBV(0xAA, 32)));
        s.add(ctx.mkEq(ctx.mkSelect(h, ctx.mkSelect(h, xp)), ctx.mkBV(0, 32)));
        s.add(ctx.mkEq(ctx.mkSelect(h, ctx.mkBVAdd((BitVecExpr)ctx.mkSelect(h, xp), ctx.mkBV(1, 32))), ctx.mkBV(0x55, 32)));

        ArrayExpr h_new = ctx.mkStore(h, xp, ctx.mkBVConst("newVal", 32));
        s.add(ctx.mkEq(ctx.mkSelect(h_new, xp), ctx.mkBV(0xDEAD, 32)));

        Status status = s.check();
        System.out.println(status);
        if (status == Status.SATISFIABLE)
            System.out.println(s.getModel());

        ctx.dispose();
    }

    @Test
    public void notOrEqualFalse() {
        Context ctx = new Context();

        BoolExpr notOr = ctx.mkNot(ctx.mkOr());
        assertTrue(notOr.simplify().isTrue());

        ctx.dispose();
    }

    @Test
    public void notAndEqualFalse() {
        Context ctx = new Context();

        BoolExpr notOr = ctx.mkNot(ctx.mkAnd());
        assertTrue(notOr.simplify().isFalse());

        ctx.dispose();
    }
}
