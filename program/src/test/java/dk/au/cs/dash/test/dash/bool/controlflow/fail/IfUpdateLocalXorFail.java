package dk.au.cs.dash.test.dash.bool.controlflow.fail;

import dk.au.cs.dash.Dash;

public class IfUpdateLocalXorFail {
    public static void test(boolean p1) {
        boolean b = p1 ^ true;
        b = b ^ true;
        if (b)
            Dash.error();
    }
}
