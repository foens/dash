package dk.au.cs.dash.test.dash.nondet;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.nondet.pass.DashFigure1Simple;
import dk.au.cs.dash.test.dash.nondet.pass.ManyIfMethodCallPass;
import org.junit.Test;

public class NonDetPass extends AbstractDashTests {

    @Test
    public void dashFigure1Simple() {
        assertDashReportsNoError(DashFigure1Simple.class);
    }

    @Test
    public void manyIfMethodCallPass() {
        assertDashReportsNoError(ManyIfMethodCallPass.class);
    }
}
