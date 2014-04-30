package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallCallWithIfsFail {
    public static void test(int k) {
        int i = foo(k);
        if (i > 100)
            Dash.error();
    }

    private static int foo(int i) {
        if(i < 0)
            return bar(-i);
        return bar(i);
    }

    private static int bar(int i) {
        if(i > 100)
            return i - i + 5;
        if(i > 95)
            return i + 5;
        return i - 5;
    }
}
