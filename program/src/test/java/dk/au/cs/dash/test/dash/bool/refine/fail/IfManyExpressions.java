package dk.au.cs.dash.test.dash.bool.refine.fail;

import dk.au.cs.dash.Dash;

public class IfManyExpressions {

    public static void test(boolean b1, boolean b2, boolean b3) {
        boolean b = (b1 && b2) || (!b1 && b3);

        if (b)
            Dash.error();
    }
}
