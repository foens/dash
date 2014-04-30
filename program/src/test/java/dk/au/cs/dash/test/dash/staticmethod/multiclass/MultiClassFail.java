package dk.au.cs.dash.test.dash.staticmethod.multiclass;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.staticmethod.multiclass.fail.CallMath;
import dk.au.cs.dash.test.dash.staticmethod.multiclass.fail.CallSumClass.CallSumClass;
import org.junit.Ignore;
import org.junit.Test;

public class MultiClassFail extends AbstractDashTests {

    @Test
    public void callSumClass() {
        assertDashReportsError(CallSumClass.class);
    }

    @Test
    @Ignore("Too many unrelated procedures that are not analyzable")
    public void callMath() {
        assertDashReportsError(CallMath.class);
    }
}
