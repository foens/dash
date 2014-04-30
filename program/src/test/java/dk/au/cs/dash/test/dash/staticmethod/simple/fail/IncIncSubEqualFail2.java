package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class IncIncSubEqualFail2 {
    public static void test(int y) {
        int x = inc(y);
        int z = inc(x);
        int v = z-x;
        if(v == 1 && y+2 == z && y > 3)
            Dash.error();
    }

    private static int inc(int i) {
        if(i > 2)
            return i+1;
        return i + 100;
    }
}
