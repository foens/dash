package dk.au.cs.dash.test.dash.basic;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.basic.fail.ErrorMethod;
import dk.au.cs.dash.test.dash.basic.pass.EmptyMethod;
import org.junit.Test;

public class BasicTests extends AbstractDashTests {
    @Test
    public void testEmptyMethod() {
        assertDashReportsNoError(EmptyMethod.class);
    }

    @Test
    public void testErrorMethod() {
        assertDashReportsError(ErrorMethod.class);
    }
}
