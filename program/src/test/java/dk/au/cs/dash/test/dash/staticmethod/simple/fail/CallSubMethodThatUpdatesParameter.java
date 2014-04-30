package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

/**
 * Created by Jacob on 17-03-14.
 */
public class CallSubMethodThatUpdatesParameter {

    public static void test(int x) {
        int y = 4;
        if(foo(x, y) == 10) {
            Dash.error();
        }
    }

    private static int foo(int a, int b) {
        b = 10 * b;
        if(b == 40)
            return a;
        else
            return 0;
    }
}
