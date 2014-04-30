package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallCallPass {
    public static void test(int k) {
        int i = foo(k);
        if (i == 3)
            Dash.error();
    }

    private static int foo(int i) {
        return bar(i);
    }

    private static int bar(int i) {
        return i*2;
    }
}
