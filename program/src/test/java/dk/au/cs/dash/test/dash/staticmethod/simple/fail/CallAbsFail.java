package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallAbsFail {
    public static void test(int k) {
        int a = abs(k);
        if(a < 0)
            Dash.error();
    }

    private static int abs(int z) {
        if (z < 0)
            return -z;
        return z;
    }
}
