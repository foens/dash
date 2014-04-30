package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallSimpleMathFunctionTwiceFail {
    public static void test(int k) {
        int i = foo(foo(k));
        if (i == foo(18))
            Dash.error();
    }

    private static int foo(int i) {
        return 2 + 2 * i;
    }
}
