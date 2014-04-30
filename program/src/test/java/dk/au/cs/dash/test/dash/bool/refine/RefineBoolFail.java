package dk.au.cs.dash.test.dash.bool.refine;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.bool.refine.fail.EmptyIfFail;
import dk.au.cs.dash.test.dash.bool.refine.fail.IfManyExpressions;
import dk.au.cs.dash.test.dash.bool.refine.fail.IfUpdateLocalFail;
import dk.au.cs.dash.test.dash.bool.refine.fail.WhileCountWithBoolean4Fail;
import org.junit.Ignore;
import org.junit.Test;

public class RefineBoolFail extends AbstractDashTests {
    @Test
    public void IfUpdateLocalFail() {
        assertDashReportsError(IfUpdateLocalFail.class);
    }

    @Test
    public void IfManyExpressions() {
        assertDashReportsError(IfManyExpressions.class);
    }

    @Test
    public void WhileCountWithBoolean4Fail() {
        assertDashReportsError(WhileCountWithBoolean4Fail.class);
    }

    @Test
    @Ignore("Cannot handle this as soon removes the code b = b and" +
            "therefore there is an if-statement that has only one" +
            "child. This makes us make a wrong graph.")
    public void emptyIfFail() {
        assertDashReportsError(EmptyIfFail.class);
    }
}
