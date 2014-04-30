package dk.au.cs.dash.test.dash.nondet;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.nondet.fail.ManyIfError;
import dk.au.cs.dash.test.dash.nondet.fail.ManyIfMethodCallFail;
import dk.au.cs.dash.test.dash.nondet.fail.SimpleIfError;
import dk.au.cs.dash.test.dash.nondet.fail.WhileError;
import org.junit.Test;

public class NonDetFail extends AbstractDashTests{

    @Test
    public void simpleIfError() {
        assertDashReportsError(SimpleIfError.class);
    }


    @Test
    public void manyIfError() {
        assertDashReportsError(ManyIfError.class);
    }


    @Test
    public void whileError() {
        assertDashReportsError(WhileError.class);
    }

    @Test
    public void manyIfErrorMethodCall() {
        assertDashReportsError(ManyIfMethodCallFail.class);
    }
}
