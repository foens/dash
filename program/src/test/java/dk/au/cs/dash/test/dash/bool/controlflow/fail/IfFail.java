package dk.au.cs.dash.test.dash.bool.controlflow.fail;

import dk.au.cs.dash.Dash;

public class IfFail {
    public static void test(boolean b) {
        if (b)
            Dash.error();
    }
}
