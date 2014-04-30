package dk.au.cs.dash.test.dash.staticmethod.recursive;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.staticmethod.recursive.fail.*;
import org.junit.Ignore;
import org.junit.Test;

public class RecursiveStaticMethodFail extends AbstractDashTests {
    @Test
    public void fib0ConstantFail() {
        assertDashReportsError(Fib0ConstantFail.class);
    }

    @Test
    public void fib0ParameterFail() {
        assertDashReportsError(Fib0ParameterFail.class);
    }

    @Test
    public void fib1ParameterFail() {
        assertDashReportsError(Fib1ParameterFail.class);
    }

    @Test
    public void fib3ConstantFail() {
        assertDashReportsError(Fib3ConstantFail.class);
    }

    @Test
    @Ignore("error: Input constraints cannot be false, even after simplification")
    public void fib3ParameterFail() {
        assertDashReportsError(Fib3ParameterFail.class);
    }

    @Test
    public void fib3ConstantEqualFail() {
        assertDashReportsError(Fib3ConstantEqualFail.class);
    }

    @Test
    public void fib0ParameterIgnoredFail() {
        assertDashReportsError(Fib0ParameterIgnoredFail.class);
    }

}
