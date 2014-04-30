package dk.au.cs.dash.test.dash.integer.refine.fail;

import dk.au.cs.dash.Dash;

public class LessThanOrEqualFail {
    public static boolean test(int i) {
        boolean b = i <= -2;

        if (b)
            Dash.error();
        return b;
    }
}
