package dk.au.cs.dash.test.dash.bool.controlflow.fail;

import dk.au.cs.dash.Dash;

public class IfNestedFail {
    public static void test(boolean b1, boolean b2) {
        if (b1) {
            if (b2) {
                Dash.error();
            }
        }
    }
}
