package dk.au.cs.dash.test.dash.integer.refine.fail;

import dk.au.cs.dash.Dash;

public class MathFail {
    public static void test(int i) {
        int x = i - 5;
        x = x & 4;
        x = x << 2;
        if(x == 16)
            Dash.error();
    }
}
