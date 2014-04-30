package dk.au.cs.dash.test.dash.bool.refine;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.bool.refine.pass.If;
import dk.au.cs.dash.test.dash.bool.refine.pass.ManyParams;
import dk.au.cs.dash.test.dash.bool.refine.pass.MutualExclusiveNestedIfs;
import dk.au.cs.dash.test.dash.bool.refine.pass.MutualExclusiveWithCalculationsNestedIfs;
import org.junit.Test;

public class RefineBoolPass extends AbstractDashTests {
    @Test
    public void If() {
        assertDashReportsNoError(If.class);
    }

    @Test
    public void mutualExclusiveNestedIfs() {
        assertDashReportsNoError(MutualExclusiveNestedIfs.class);
    }

    @Test
    public void mutualExclusiveWithCalculationsNestedIfs() {
        assertDashReportsNoError(MutualExclusiveWithCalculationsNestedIfs.class);
    }

    @Test
    public void manyParams() {
        assertDashReportsNoError(ManyParams.class);
    }
}
