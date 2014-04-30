package dk.au.cs.dash.test.dash.staticmethod.complex;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.staticmethod.complex.fail.*;
import dk.au.cs.dash.test.dash.staticmethod.complex.pass.CallMultipleTimesInsideWhilePass;
import dk.au.cs.dash.test.dash.staticmethod.complex.pass.SimplifiedCallMultipleTimesInsideWhilePass;
import org.junit.Ignore;
import org.junit.Test;

public class ComplexStaticMethodFail extends AbstractDashTests {
    @Test
    @Ignore("Loop fail, loop iterations dependent on input value, gets stuck around p0=-3 after 30-40 iterations")
    public void strangeSubtractionFail() {
        assertDashReportsError(StrangeSubtractionFail.class);
    }

    @Test
    public void callInsideWhileFail() {
        assertDashReportsError(CallInsideWhileFail.class);
    }

    @Test
    public void callMultipleTimesInsideWhileFail() {
        assertDashReportsError(CallMultipleTimesInsideWhileFail.class);
    }

    @Test
    public void isPrimeFail() {
        assertDashReportsError(IsPrimeFail.class);
    }

    @Test
    @Ignore("Loop fail, loop iterations dependent on input value")
    public void callMultipleTimesInsideWhilePass() {
        assertDashReportsError(CallMultipleTimesInsideWhilePass.class);
    }

    @Test
    public void simplifiedCallMultipleTimesInsideWhilePass() {
        assertDashReportsError(SimplifiedCallMultipleTimesInsideWhilePass.class);
    }

    @Test
    public void callInLoopWithSumOverRange() {
        assertDashReportsError(CallInLoopWithSumOverRange.class);
    }
}
