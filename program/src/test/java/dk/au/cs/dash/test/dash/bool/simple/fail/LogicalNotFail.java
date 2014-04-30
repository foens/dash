package dk.au.cs.dash.test.dash.bool.simple.fail;

import dk.au.cs.dash.Dash;

public class LogicalNotFail {
    public static boolean test(boolean b) {
        Dash.error();
        return !b;
    }
}
