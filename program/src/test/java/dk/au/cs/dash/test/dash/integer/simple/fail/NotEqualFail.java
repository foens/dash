package dk.au.cs.dash.test.dash.integer.simple.fail;

import dk.au.cs.dash.Dash;

public class NotEqualFail {
    public static boolean test(int i) {
        boolean b = i != 2;
        Dash.error();
        return b;
    }
}
