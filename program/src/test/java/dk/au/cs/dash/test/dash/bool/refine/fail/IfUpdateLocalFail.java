package dk.au.cs.dash.test.dash.bool.refine.fail;

import dk.au.cs.dash.Dash;

public class IfUpdateLocalFail {
    public static void test(boolean p1) {
        boolean b = !p1;
        b = !b;
        if (b)
            Dash.error();
    }
}
