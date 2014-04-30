package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class IncIncSubEqualPass {
    public static void test(int y) {
        int x = inc(y);
        int z = inc(x);
        int v = z-x;
        if(v != 1)
            Dash.error();
    }

    private static int inc(int i) {
        return i+1;
    }
}
