package dk.au.cs.dash.test.dash.staticmethod.complex;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.staticmethod.complex.pass.*;
import org.junit.Ignore;
import org.junit.Test;

public class ComplexStaticMethodPass extends AbstractDashTests {
    @Test
    @Ignore("Loop fail, loop iterations dependent on input value")
    public void strangeSubtractionPass() {
        assertDashReportsNoError(StrangeSubtractionPass.class);
    }

    @Test
    public void callInsideWhilePass() {
        assertDashReportsNoError(CallInsideWhilePass.class);
    }

    @Test
    public void callAreaFunction() {
        assertDashReportsNoError(CallAreaFunction.class);
    }

    @Test
    public void findSquareRootNegativePass() {
        assertDashReportsNoError(FindSquareRootNegativePass.class);
    }

    @Test
    public void findSquareRootPrimePass() {
        assertDashReportsNoError(FindSquareRootPrimePass.class);
    }

    @Ignore("Loop fail, loop iterations dependent on input value")
    public void callMultipleTimesInsideWhilePass() {
        assertDashReportsError(CallMultipleTimesInsideWhilePass.class);
    }

    @Test
    @Ignore("Loop fail, loop iterations dependent on input value")
    public void isPrimePass() {
        assertDashReportsError(IsPrimePass.class);
    }
}
