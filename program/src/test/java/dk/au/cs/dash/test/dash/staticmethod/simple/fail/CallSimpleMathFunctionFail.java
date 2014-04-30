package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallSimpleMathFunctionFail {
    public static void test(int k) {
        int i = foo(k);
        if (i == 18)
            Dash.error();
    }

    private static int foo(int i) {
        return 2 + 2 * i;
    }
}
