package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class IncIncSubEqualPass2 {
    public static void test(int y) {
        int x = magic_inc(y);
        int z = magic_inc(x);
        int v = z-x;
        if(v != 1 && y > 3 && y < 1000)
            Dash.error();
    }

    private static int magic_inc(int i) {
        if(i > 2)
            return i+1;
        return i + 100;
    }
}
