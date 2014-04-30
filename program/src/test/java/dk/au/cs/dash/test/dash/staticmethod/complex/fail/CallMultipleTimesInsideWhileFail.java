package dk.au.cs.dash.test.dash.staticmethod.complex.fail;

import dk.au.cs.dash.Dash;

public class CallMultipleTimesInsideWhileFail {
    public static void test(int x, int g) {
        int startG = g;
        while(g > 3)
        {
            g--;
            x = foo(x);
        }
        if(x == -10 && startG > 5)
            Dash.error();
    }

    private static int foo(int z) {
        return z + 5;
    }
}
