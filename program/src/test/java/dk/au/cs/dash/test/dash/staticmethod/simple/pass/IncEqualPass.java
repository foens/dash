package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class IncEqualPass {
    public static void test(int y) {
        int x = inc(y);
        if (x == y)
            Dash.error();
    }

    private static int inc(int i) {
        return i + 1;
    }
}
