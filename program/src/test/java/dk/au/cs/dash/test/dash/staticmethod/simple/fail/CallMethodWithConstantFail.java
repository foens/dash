package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallMethodWithConstantFail {
    public static void test(int a, int b) {
        int i = inc(3) + a + b;

        if(i == 3)
            Dash.error();
    }

    private static int inc(int a) {
        return a + 1;
    }
}
