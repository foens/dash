package dk.au.cs.dash.test.dash.bool.simple.fail;

import dk.au.cs.dash.Dash;

public class LogicalAndFail {

    public static boolean test(boolean b1, boolean b2) {
        Dash.error();
        return b1 && b2;
    }
}
