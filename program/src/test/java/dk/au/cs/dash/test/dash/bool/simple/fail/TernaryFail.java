package dk.au.cs.dash.test.dash.bool.simple.fail;

import dk.au.cs.dash.Dash;

public class TernaryFail {
    public static boolean test(boolean b1, boolean b2, boolean b3) {
        Dash.error();
        return b1 ? b2 : b2;
    }
}
