package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallSimpleMathFunctionPass {
    public static void test(int k) {
        int i = foo(k);
        if (i == 17)
            Dash.error();
    }

    private static int foo(int i) {
        return 2 + 2 * i;
    }
}
