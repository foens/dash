package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallMethodTwiceFail {
    public static void test(int a, int b) {
        int i = inc(a);
        int j = inc(b);
        a = 200000000;

        if(i == 3 && j == 4 && i + j == 7)
            Dash.error();
    }

    private static int inc(int a) {
        return a + 1;
    }
}
