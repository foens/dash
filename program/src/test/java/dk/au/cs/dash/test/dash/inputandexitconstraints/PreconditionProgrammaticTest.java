package dk.au.cs.dash.test.dash.inputandexitconstraints;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.basic.fail.ErrorMethod;
import dk.au.cs.dash.test.dash.basic.pass.EmptyMethod;
import dk.au.cs.dash.test.dash.bool.refine.fail.IfManyExpressions;
import dk.au.cs.dash.test.dash.bool.simple.pass.BoolParam;
import dk.au.cs.dash.test.dash.integer.controlflow.fail.AddFail;
import dk.au.cs.dash.test.dash.integer.refine.fail.ForAddFiveIterationsFail;
import org.junit.Test;

import java.io.IOException;

public class PreconditionProgrammaticTest extends AbstractDashTests{


    @Test
    public void canPutUnneedPreconditionOnSimplest() {
        assertDash(EmptyMethod.class, "test", true, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkTrue();
            }
        }, null);
    }

    @Test
    public void canPassBoolParamWithOneParamValueExcluded() {
        assertDash(BoolParam.class, "test", true, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkEq(ctx.mkBVConst("b1", 32), ctx.mkBV(0, 32));
            }
        }, null);
    }

    @Test
    public void canPreventErrorOnAddFailTest() throws IOException {
        assertDash(AddFail.class, "test", true, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkEq(ctx.mkBVConst("i", 32), ctx.mkBV(0, 32));
            }
        }, null);
    }

    @Test
    public void canPreventErrorInForAddFiveIterationsFail() {
        assertDash(ForAddFiveIterationsFail.class, "test", true, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkEq(ctx.mkBVConst("i", 32), ctx.mkBV(1, 32));
            }
        }, null);
    }


    @Test
    public void complicatedPreconditionSafesTheDayInIfManyExpressions() {
        assertDash(IfManyExpressions.class, "test", true, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkAnd(ctx.mkEq(ctx.mkBVConst("b1", 32), ctx.mkBV(0, 32)),
                        ctx.mkEq(ctx.mkBVConst("b2", 32), ctx.mkBV(1, 32)),
                        ctx.mkEq(ctx.mkBVConst("b3", 32), ctx.mkBV(0, 32)));
            }
        }, null);
    }


    @Test
    public void canNotPreventErrorOnSimplest() {
        assertDash(ErrorMethod.class, "test", false, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkTrue();
            }
        }, null);
    }

    @Test(expected = RuntimeException.class)
    public void canPreventErrorOnSimplestWithFalse() {
        assertDash(ErrorMethod.class, "test", true, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkFalse();
            }
        }, null);
    }

    @Test(expected = RuntimeException.class)
    public void canPreventErrorOnAddFailWithFalse() {
        assertDash(AddFail.class, "test", true, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkFalse();
            }
        }, null);
    }

    @Test
    public void overflowUsedOvercomePreconditionAndFailOnAddFailTest() throws IOException {

        assertDash(AddFail.class, "test", false, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkBVSGE(ctx.mkBVConst("i", 32), ctx.mkBV(0, 32));
            }
        }, null);
    }
}
