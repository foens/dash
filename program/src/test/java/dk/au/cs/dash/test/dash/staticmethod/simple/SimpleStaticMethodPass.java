package dk.au.cs.dash.test.dash.staticmethod.simple;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.staticmethod.simple.pass.*;
import org.junit.Ignore;
import org.junit.Test;

public class SimpleStaticMethodPass extends AbstractDashTests {
    @Test
    @Ignore("No support for call out side assigns")
    public void callThenPass() {
        assertDashReportsNoError(CallThenPass.class);
    }

    @Test
    public void callWithReturnThenPass() {
        assertDashReportsNoError(CallWithReturnThenPass.class);
    }

    @Test
    public void callWithReturnNoConstantInitializationThenPass() {
        assertDashReportsNoError(CallWithReturnNoConstantInitializationThenPass.class);
    }

    @Test
    public void callWithReturnBooleanThenPass() {
        assertDashReportsNoError(CallWithReturnBooleanThenPass.class);
    }

    @Test
    public void callWithExtendOverCallOpPass() {
        assertDashReportsNoError(CallWithExtendOverCallOpPass.class);
    }

    @Test
    public void incEqualTwoParametersPass() {
        assertDashReportsNoError(IncEqualTwoParametersPass.class);
    }

    @Test
    public void incEqualWithInputConstraintsPass() {
        assertDashReportsNoError(IncEqualWithInputConstraintsPass.class);
    }

    @Test
    public void incEqualPass() {
        assertDashReportsNoError(IncEqualPass.class);
    }

    @Test
    public void callSimpleMathFunctionPass() {
        assertDashReportsNoError(CallSimpleMathFunctionPass.class);
    }

    @Test
    public void callSimpleMathFunctionTwicePass() {
        assertDashReportsNoError(CallSimpleMathFunctionTwicePass.class);
    }

    @Test
    public void callCallPass() {
        assertDashReportsNoError(CallCallPass.class);
    }

    @Test
    public void callCallWithIfsPass() {
        assertDashReportsNoError(CallCallWithIfsPass.class);
    }

    @Test
    public void callWithLoopPass() {
        assertDashReportsNoError(CallWithLoopPass.class);
    }

    @Test
    public void incIncSubEqualPass() {
        assertDashReportsNoError(IncIncSubEqualPass.class);
    }

    @Test
    public void incIncSubEqualPass2() {
        assertDashReportsNoError(IncIncSubEqualPass2.class);
    }

    @Test
    public void callWithManyArgumentsPass() {
        assertDashReportsNoError(CallWithManyArgumentsPass.class);
    }

    @Test
    public void callWithArgumentsOfDifferentTypesPass() {
        assertDashReportsNoError(CallWithArgumentsOfDifferentTypesPass.class);
    }

    @Test
    public void callSameMethodWithIfsPass() {
        assertDashReportsNoError(CallSameMethodWithIfsPass.class);
    }

    @Test
    public void callAbsPass() {
        assertDashReportsNoError(CallAbsPass.class);
    }

    @Test
    public void call3ParamFooPass() {
        assertDashReportsNoError(Call3ParamFooPass.class);
    }

    @Test
    public void impossibleToReachErrors() {
        assertDashReportsNoError(ImpossibleToReachErrors.class);
    }

    @Test
    public void callAddPass() {
        assertDashReportsNoError(CallAddPass.class);
    }

    @Test
    public void callAbsPassWithPrecondition() {
        assertDashReportsNoError(CallAbsPassWithPrecondition.class);
    }
}
