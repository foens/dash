package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallWithPreconditionFail {
    public static void test(int k) {
        if(k == -1)
            return;

        int i = foo(k);
        if(i == 0)
            Dash.error();
    }

    private static int foo(int i) {
        if(i < 0)
            return 0;
        return 1;
    }
}
