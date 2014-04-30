package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallCallCallFail {
    public static void test(int k) { // k = 4 makes this test fail
        int i = foobarinc(k);
        if (i == 0)
            Dash.error();
    }

    private static int foobarinc(int i) {
        return bar(foo(i + 1), i-1);
    }

    private static int foo(int i) {
        if(i > 2)
            return 2;
        if (i >= 0)
            return 0;
        if (i < -50)
            return -4;
        return i + 3;
    }

    private static int bar(int i1, int i2) {
        if(i1 == 2 && i2 == 3)
            return 0;
        return 1;
    }
}
