package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

/**
 * Created by Jacob on 31-03-2014.
 */
public class BoolAsLocalVarButForcedToTakeNonBoolValuePass {
    public static void test(int x) {
        boolean b = x > 0;
        if(!b) {
            if(x > 0) {
                    Dash.error();
            }
        }
    }
}
