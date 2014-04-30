package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

public class ForWithIfPass {
    public static void test() {
        for (int i = 0; i < 1; ++i) {
            //noinspection ConstantConditions
            if (i == 1) // Not possible
                Dash.error();
        }
    }
}
