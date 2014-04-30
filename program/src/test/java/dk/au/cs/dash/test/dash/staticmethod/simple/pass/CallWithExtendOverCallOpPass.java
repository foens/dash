package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallWithExtendOverCallOpPass {
    public static void test(int k) {
        int i = 0;
        i = inc(k);
        if(i == 2)
            Dash.error();
    }

    private static int inc(int i) {
        if (i == 0)
            return 1;
        return 3;
    }
}
