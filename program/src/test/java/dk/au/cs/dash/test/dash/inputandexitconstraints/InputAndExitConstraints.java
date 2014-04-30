package dk.au.cs.dash.test.dash.inputandexitconstraints;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.basic.fail.ErrorMethod;
import dk.au.cs.dash.test.dash.basic.pass.EmptyMethod;
import dk.au.cs.dash.test.dash.inputandexitconstraints.testclasses.SquareNumber;
import org.junit.Test;

import static dk.au.cs.dash.util.DashConstants.createResultVariable;

public class InputAndExitConstraints extends AbstractDashTests {

    @Test
    public void canDoEmptyMethodTrueFalse() {
        assertDash(EmptyMethod.class, "test", true, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkTrue();
                    }
                }, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkFalse();
                    }
                });
    }

    @Test
    public void canDoEmptyMethodTrueTrue() {
        assertDash(EmptyMethod.class, "test", false, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkTrue();
                    }
                }, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkTrue();
                    }
                });
    }

    @Test(expected = RuntimeException.class)
    public void canDoEmptyMethodFalseFalse() {
        assertDash(EmptyMethod.class, "test", true, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkFalse();
                    }
                }, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkFalse();
                    }
                });
    }

    @Test(expected = RuntimeException.class)
    public void canDoEmptyMethodFalseTrue() {
        assertDash(EmptyMethod.class, "test", true, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkFalse();
                    }
                }, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkTrue();
                    }
                });
    }


    @Test
    public void canDoErrorMethodTrueFalse() {
        assertDash(ErrorMethod.class, "test", false, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkTrue();
                    }
                }, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkFalse();
                    }
                });
    }

    @Test
    public void canDoErrorMethodTrueTrue() {
        assertDash(ErrorMethod.class, "test", false, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkTrue();
                    }
                }, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkTrue();
                    }
                });
    }

    @Test(expected = RuntimeException.class)
    public void canDoErrorMethodFalseFalse() {
        assertDash(ErrorMethod.class, "test", true, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkFalse();
                    }
                }, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkFalse();
                    }
                });
    }

    @Test(expected = RuntimeException.class)
    public void canDoErrorMethodFalseTrue() {
        assertDash(ErrorMethod.class, "test", true, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkFalse();
                    }
                }, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkTrue();
                    }
                });
    }


    @Test
    public void canLimitInputToSquareAndWillGetPositiveNumberOut() {
        assertDash(SquareNumber.class, "test", true, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkAnd(ctx.mkBVSGE(ctx.mkBVConst("j", 32), ctx.mkBV(-1000, 32)),
                                ctx.mkBVSLE(ctx.mkBVConst("j", 32), ctx.mkBV(1000, 32)));
                    }
                }, new ConditionGenerator() {
                    @Override
                    public BoolExpr create(Context ctx) {
                        return ctx.mkBVSLT(createResultVariable(ctx), ctx.mkBV(0, 32));
                    }
                });
    }
}
