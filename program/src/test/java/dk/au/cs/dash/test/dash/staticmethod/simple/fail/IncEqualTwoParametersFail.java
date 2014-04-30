package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class IncEqualTwoParametersFail {
    public static void test(int x, int y) {
        int z = inc(y);
        if(z == x)
            Dash.error();
    }

    private static int inc(int i) {
        return i+1;
    }
}
