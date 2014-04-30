package dk.au.cs.dash.test.dash.inputandexitconstraints;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.basic.fail.ErrorMethod;
import dk.au.cs.dash.test.dash.basic.pass.EmptyMethod;
import dk.au.cs.dash.test.dash.integer.refine.pass.AndPass;
import dk.au.cs.dash.test.dash.integer.refine.pass.AssignmentShiftRightSignExtendPass;
import dk.au.cs.dash.test.dash.integer.simple.pass.ExclusiveOr;
import dk.au.cs.dash.test.dash.inputandexitconstraints.testclasses.PerformOperation;
import dk.au.cs.dash.test.dash.inputandexitconstraints.testclasses.Return4;
import dk.au.cs.dash.test.dash.inputandexitconstraints.testclasses.SquareNumber;
import org.junit.Test;

import static dk.au.cs.dash.util.DashConstants.createResultVariable;

public class ExitConstraintsProgrammaticTest extends AbstractDashTests{
    @Test
    public void canPassFalseExitConstraintOnEmptyMethod() {
        assertDash(EmptyMethod.class, "test", true, null, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkFalse();
            }
        });
    }

    @Test
    public void canFailWithFalseExitConstraintOnFailMethod() {
        assertDash(ErrorMethod.class, "test", false, null, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkFalse();
            }
        });
    }


    @Test
    public void emptyMethodFailsOnTrueExitConstraint() {
        assertDash(EmptyMethod.class, "test", false, null, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkTrue();
            }
        });
    }


    @Test
    public void canUseResultToFailExitConstraintInAndPass() {
        assertDash(AndPass.class, "test", false, null, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkBVSGT(createResultVariable(ctx), ctx.mkBV(0, 32));
            }
        });
    }

    @Test
    public void canFailExitConstraintWithOverflow() {
        assertDash(SquareNumber.class, "test", false, null, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkBVSLT(createResultVariable(ctx), ctx.mkBV(0, 32));
            }
        });
    }

    @Test
    public void canReturn4ExitConstraint() {
        assertDash(Return4.class, "test", true, null, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkNot(ctx.mkEq(createResultVariable(ctx), ctx.mkBV(4, 32)));
            }
        });
    }

    @Test
    public void singleLogicalShiftRightWillAlwaysMakeIntPositive() {
        assertDash(AssignmentShiftRightSignExtendPass.class, "test", true, null, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkBVSLT(createResultVariable(ctx), ctx.mkBV(0, 32));
            }
        });
    }

    @Test
    public void performOperation() {
        assertDash(PerformOperation.class, "test", false, null, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkBVSLT(createResultVariable(ctx), ctx.mkBVConst("a", 32));
            }
        });
    }

    @Test
    public void andPassCanOnlyReturnTwoValues() {
        assertDash(AndPass.class, "test", true, null, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkNot(ctx.mkOr(ctx.mkEq(createResultVariable(ctx), ctx.mkBV(0, 32)),
                        ctx.mkEq(createResultVariable(ctx), ctx.mkBV(4, 32))));
            }
        });
    }

    @Test
    public void exclusiveOrFail() {
        assertDash(ExclusiveOr.class, "test", false, null, new ConditionGenerator() {
            @Override
            public BoolExpr create(Context ctx) {
                return ctx.mkNot(ctx.mkEq(createResultVariable(ctx), ctx.mkBV(0, 32)));
            }
        });
    }
}
