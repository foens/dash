package dk.au.cs.dash.test.dash.staticmethod.multiclass.fail;

import dk.au.cs.dash.Dash;

/**
 * Created by Jacob on 18-03-14.
 */
public class CallMath {
    public void test(int x) {
        if(x < 0)
            x = Math.abs(x);
        if(x < 0)
            Dash.error();
    }
}
