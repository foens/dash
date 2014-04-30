package dk.au.cs.dash.test.dash.examples.thesis;

import dk.au.cs.dash.AbstractDashTests;
import org.junit.Test;

public class ThesisExamplesTester extends AbstractDashTests {
    @Test
    public void infiniteRefineLoop() {
        assertDashReportsNoError(InfiniteRefineLoop.class);
    }

    @Test
    public void slightlyModifiedSynergy() {
        assertDashReportsError(SlightlyModifiedSynergy.class);
    }

    @Test
    public void synergyPass() {
        assertDashReportsError(SynergyPass.class);
    }
}
