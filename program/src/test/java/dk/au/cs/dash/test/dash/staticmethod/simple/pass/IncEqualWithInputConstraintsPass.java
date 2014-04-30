package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class IncEqualWithInputConstraintsPass {
    public static void test(int x, int y) {
        y=40;
        int z = inc(y);
        if(z == y)
            Dash.error();
    }

    private static int inc(int i) {
        return i+1;
    }
}
