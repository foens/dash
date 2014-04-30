package dk.au.cs.dash.test.dash.integer.refine.fail;

import dk.au.cs.dash.Dash;

public class NotEqualFail {
    public static boolean test(int i) {
        boolean b = i != 0;

        if (b)
            Dash.error();

        return b;
    }
}
