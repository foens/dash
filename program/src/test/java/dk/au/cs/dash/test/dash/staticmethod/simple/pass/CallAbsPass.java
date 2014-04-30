package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallAbsPass {
    public static void test(int k) {
        int a = abs(k);
        if(a < 0 && a != Integer.MIN_VALUE)
            Dash.error();
    }

    private static int abs(int z) {
        if (z < 0)
            return -z;
        return z;
    }
}
