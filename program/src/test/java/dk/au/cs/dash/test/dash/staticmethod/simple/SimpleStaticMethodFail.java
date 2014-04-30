package dk.au.cs.dash.test.dash.staticmethod.simple;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.staticmethod.simple.fail.*;
import org.junit.Ignore;
import org.junit.Test;

public class SimpleStaticMethodFail extends AbstractDashTests {
    @Test
    @Ignore("No support for call out side assigns")
    public void callThenFail() {
        assertDashReportsError(CallThenFail.class);
    }

    @Test
    public void callWithReturnThenFail() {
        assertDashReportsError(CallWithReturnThenFail.class);
    }

    @Test
    public void callWithReturnBooleanThenFail() {
        assertDashReportsError(CallWithReturnBooleanThenFail.class);
    }

    @Test
    public void methodReturnsInputThenFail() {
        assertDashReportsError(MethodReturnsInputThenFail.class);
    }

    @Test
    public void methodReturnsInputThenFail2Input() {
        assertDashReportsError(MethodReturnsInputThenFail2Input.class);
    }

    @Test
    public void callMethodTwiceFail() {
        assertDashReportsError(CallMethodTwiceFail.class);
    }

    @Test
    public void callMethodWithConstantFail() {
        assertDashReportsError(CallMethodWithConstantFail.class);
    }

    @Test
    public void callNestedMethodFail() {
        assertDashReportsError(CallNestedMethodFail.class);
    }

    @Test
    public void callFibThenFail() {
        assertDashReportsError(CallFibonacciThenFail.class);
    }

    @Test
    public void callWithExtendOverCallOpFail() {
        assertDashReportsError(CallWithExtendOverCallOpFail.class);
    }

    @Test
    public void incEqualFail() {
        assertDashReportsError(IncEqualFail.class);
    }

    @Test
    public void incEqualTwoParametersFail() {
        assertDashReportsError(IncEqualTwoParametersFail.class);
    }

    @Test
    public void callWithPreconditionFail() {
        assertDashReportsError(CallWithPreconditionFail.class);
    }

    @Test
    public void callSimpleMathFunctionFail() {
        assertDashReportsError(CallSimpleMathFunctionFail.class);
    }

    @Test
    public void callSimpleMathFunctionTwiceFail() {
        assertDashReportsError(CallSimpleMathFunctionTwiceFail.class);
    }

    @Test
    public void CallCallFail() {
        assertDashReportsError(CallCallFail.class);
    }

    @Test
    public void callCallWithIfsFail() {
        assertDashReportsError(CallCallWithIfsFail.class);
    }

    @Test
    public void callWithLoopFail() {
        assertDashReportsError(CallWithLoopFail.class);
    }

    @Test
    public void incIncSubEqualFail() {
        assertDashReportsError(IncIncSubEqualFail.class);
    }

    @Test
    public void incIncSubEqualFail2() {
        assertDashReportsError(IncIncSubEqualFail2.class);
    }

    @Test
    public void callCallCallFail() {
        assertDashReportsError(CallCallCallFail.class);
    }

    @Test
    public void callWithManyArgumentsFail() {
        assertDashReportsError(CallWithManyArgumentsFail.class);
    }

    @Test
    public void callWithArgumentsOfDifferentTypesFail() {
        assertDashReportsError(CallWithArgumentsOfDifferentTypesFail.class);
    }

    @Test
    public void callSameMethodWithIfsFail() {
        assertDashReportsError(CallSameMethodWithIfsFail.class);
    }

    @Test
    public void callAbsFail() {
        assertDashReportsError(CallAbsFail.class);
    }

    @Test
    public void callSubMethodThatUpdatesParameter() {
        assertDashReportsError(CallSubMethodThatUpdatesParameter.class);
    }
}
