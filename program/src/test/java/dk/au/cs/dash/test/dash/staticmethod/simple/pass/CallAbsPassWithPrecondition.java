package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallAbsPassWithPrecondition {
    public static void test(int x) {
        if(x != -2147483648)
        {
            x = abs(x);
            if(x < 0)
                Dash.error();
        }
    }

    private static int abs(int a) {
        if (a < 0)
            return -a;
        return a;
    }
}
