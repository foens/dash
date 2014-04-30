package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class IncEqualFail {
    public static void test(int y) {
        int x = inc(y);
        if(x == y+1)
            Dash.error();
    }

    private static int inc(int i) {
        return i+1;
    }
}
