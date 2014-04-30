package dk.au.cs.dash.test.dash.bool.refine.pass;

import dk.au.cs.dash.Dash;

public class MutualExclusiveNestedIfs {

    public static void test(boolean b) {
        if(b) {
            if(!b) {
                Dash.error();
            }
        }
    }
}
