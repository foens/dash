package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

public class PassWithWhileLoop {
    public static void test() {
        int b = 0;
        int i = 0;
        while (i < 1)
            i++;
        //noinspection ConstantConditions
        if (b == 1)
            Dash.error();
    }
}
