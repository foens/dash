package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallWithExtendOverCallOpFail {
    public static void test(int k) {
        int i = inc(k);
        if(i == 2)
            Dash.error();
    }

    private static int inc(int i) {
        if (i == 0)
            return 1;
        if (i == 1)
            return 2;
        return 3;
    }
}
