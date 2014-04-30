package dk.au.cs.dash.test.dash.staticmethod.recursive;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.staticmethod.recursive.fail.Fib0ParameterIgnoredFail;
import dk.au.cs.dash.test.dash.staticmethod.recursive.pass.*;
import org.junit.Ignore;
import org.junit.Test;

public class RecursiveStaticMethodPass extends AbstractDashTests {
    @Test
    public void fib0ConstantPass() {
        assertDashReportsNoError(Fib0ConstantPass.class);
    }

    @Test
    @Ignore("handle recursion, recursion depth dependent on input value")
    public void fib0ParameterPass() {
        assertDashReportsNoError(Fib0ParameterPass.class);
    }

    @Test
    public void fib3ConstantPass() {
        assertDashReportsNoError(Fib3ConstantPass.class);
    }

    @Test
    @Ignore("handle recursion, recursion depth dependent on input value")
    public void fFib3ParameterPass() {
        assertDashReportsNoError(Fib3ParameterPass.class);
    }

    @Test
    @Ignore("Input constraints cannot be false, even after simplification")
    public void fib0ParameterIgnoredPass() {
        assertDashReportsNoError(Fib0ParameterIgnoredPass.class);
    }
}
