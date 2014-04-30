package dk.au.cs.dash.test.dash.staticmethod.multiclass;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.staticmethod.multiclass.pass.CallManyClass.CallManyClass;
import org.junit.Test;

/**
 * Created by Jacob on 18-03-14.
 */
public class MultiClassPass extends AbstractDashTests{

    @Test
    public void callManyClass() {
        assertDashReportsNoError(CallManyClass.class);
    }

}
