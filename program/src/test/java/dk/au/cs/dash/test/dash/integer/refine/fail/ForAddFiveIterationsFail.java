package dk.au.cs.dash.test.dash.integer.refine.fail;

import dk.au.cs.dash.Dash;

public class ForAddFiveIterationsFail {
    public static int test(int i) {
        int k = 0;
        for (int j = 1; j < i; ++j) {
            k += j;
        }
        if (k == 21)
            Dash.error();
        return i;
    }
}