package dk.au.cs.dash.test.dash.bool.refine.pass;

import dk.au.cs.dash.Dash;

public class MutualExclusiveWithCalculationsNestedIfs {

    public static void test(boolean b) {
        if(b) {
            b = b || b && b || b;
            b = b ? true : false;

            if(!b) {
                Dash.error();
            }
        }
    }
}
