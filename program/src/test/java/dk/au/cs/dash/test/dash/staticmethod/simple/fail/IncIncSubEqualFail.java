package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class IncIncSubEqualFail {
    public static void test(int y) {
        int x = inc(y);
        int z = inc(x);
        int v = z-x;
        if(v == 1 && y+2 == z && y > 3)
            Dash.error();
    }

    private static int inc(int i) {
        return i+1;
    }
}
