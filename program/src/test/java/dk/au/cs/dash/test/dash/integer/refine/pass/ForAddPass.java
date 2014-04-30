package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

public class ForAddPass {
    public static void test(int i) {
        int k = 0;
        for (int j = 1; j < i && j < 10; ++j)
            k += j;
        if (k == 2) // Not possible
            Dash.error();
    }
}
