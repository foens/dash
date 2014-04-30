package dk.au.cs.dash.test.dash.examples;

import dk.au.cs.dash.AbstractDashTests;
import org.junit.Ignore;
import org.junit.Test;

public class ExamplesTester extends AbstractDashTests {
    @Test
    public void dartPage214Simplified() {
        assertDashReportsError(DartPage214Simplified.class);
    }

    @Test
    public void dartPage214() {
        assertDashReportsError(DartPage214.class);
    }

    @Test
    public void synergyFigure1() {
        assertDashReportsError(SynergyFigure1.class);
    }

    @Test
    public void synergyFigure1Pass() {
        assertDashReportsNoError(SynergyFigure1Pass.class);
    }

    @Test
    @Ignore("Requires Objects and aliasing")
    public void dashFigure1() {
        assertDashReportsError(DashFigure1.class);
    }

    @Test
    @Ignore("Requires Objects and aliasing")
    public void dashFigure3() {
        assertDashReportsError(DashFigure3.class);
    }

    @Test
    public void dashFigure6() {
        assertDashReportsNoError(DashFigure6.class);
    }

    @Test
    public void dashFigure6SingleInc() {
        assertDashReportsNoError(DashFigure6SingleInc.class);
    }
}
