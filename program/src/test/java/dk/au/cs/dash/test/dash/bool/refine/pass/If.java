package dk.au.cs.dash.test.dash.bool.refine.pass;

import dk.au.cs.dash.Dash;

public class If {
    @SuppressWarnings({"ConstantConditions"})
    public static void test() {
        boolean b = false;
        if (b)
            Dash.error();
    }
}
