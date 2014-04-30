package dk.au.cs.dash.test.dash.bool.controlflow;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.bool.controlflow.fail.*;
import org.junit.Test;

public class ControlFlowBoolFail extends AbstractDashTests {
    @Test
    public void IfFail() {
        assertDashReportsError(IfFail.class);
    }

    @Test
    public void IfAndFail() {
        assertDashReportsError(IfAndFail.class);
    }

    @Test
    public void IfXorFail() {
        assertDashReportsError(IfXorFail.class);
    }

    @Test
    public void IfNestedFail() {
        assertDashReportsError(IfNestedFail.class);
    }

    @Test
    public void IfUpdateLocalXorFail() {
        assertDashReportsError(IfUpdateLocalXorFail.class);
    }

    @Test
    public void WhileFail() {
        assertDashReportsError(WhileFail.class);
    }

    @Test
    public void ForFail() {
        assertDashReportsError(ForFail.class);
    }

    @Test
    public void DoWhileFailInside() {
        assertDashReportsError(DoWhileFailInside.class);
    }

    @Test
    public void DoWhileFailOutside() {
        assertDashReportsError(DoWhileFailOutside.class);
    }
}