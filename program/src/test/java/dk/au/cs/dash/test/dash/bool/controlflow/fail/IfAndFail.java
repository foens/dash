package dk.au.cs.dash.test.dash.bool.controlflow.fail;

import dk.au.cs.dash.Dash;

public class IfAndFail {
    public static void test(boolean b1, boolean b2) {
        if (b1 && b2)
            Dash.error();
    }
}
