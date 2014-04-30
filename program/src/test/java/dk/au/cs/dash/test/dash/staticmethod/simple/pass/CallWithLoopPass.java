package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallWithLoopPass {
    public static void test(int k) {
        int i = foo(k);
        if (i == 2)
            Dash.error();
    }

    private static int foo(int i) {
        int k = i;
        while(k > 0)
            k--;
        return i-i+1;
    }
}
