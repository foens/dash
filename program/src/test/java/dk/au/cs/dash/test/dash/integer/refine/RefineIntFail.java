package dk.au.cs.dash.test.dash.integer.refine;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.integer.refine.fail.*;
import org.junit.Ignore;
import org.junit.Test;

public class RefineIntFail extends AbstractDashTests {
    @Test
    public void lessThanFail() {
        assertDashReportsError(LessThanFail.class);
    }

    @Test
    public void greaterThanOrEqualFail() {
        assertDashReportsError(GreaterThanOrEqualFail.class);
    }

    @Test
    public void greaterThanFail() {
        assertDashReportsError(GreaterThanFail.class);
    }

    @Test
    public void equalFail() {
        assertDashReportsError(EqualFail.class);
    }

    @Test
    public void lessThanOrEqualFail() {
        assertDashReportsError(LessThanOrEqualFail.class);
    }

    @Test
    public void notEqualFail() {
        assertDashReportsError(NotEqualFail.class);
    }

    @Test
    @Ignore("Works correctly but takes 26 seconds")
    public void forAddFiveIterationsFail() {
        assertDashReportsError(ForAddFiveIterationsFail.class);
    }

    @Test
    public void forAddOneIterationFail() {
        assertDashReportsError(ForAddOneIterationFail.class);
    }

    @Test
    @Ignore("Works when running alone, but not together with all tests")
    public void forAddTwoIterationsFail() {
        assertDashReportsError(ForAddTwoIterationsFail.class);
    }

    @Test
    public void subtleAbsOverflowFail() {
        assertDashReportsError(SubtleAbsOverflowFail.class);
    }

    @Test
    public void mathFail() {
        assertDashReportsError(MathFail.class);
    }

    @Test
    public void primeFail() {
        assertDashReportsError(PrimeFail.class);
    }

    @Test
    public void multiplyNumberEqualToPrimeFail() {
        assertDashReportsError(MultiplyNumberEqualToPrimeFail.class);
    }
}