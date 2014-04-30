package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class Call3ParamFooPass {
    public static void test(int k) {
        int a = foo(1, 2, 3);
        if(a == 3)
            Dash.error();
    }

    private static int foo(int x, int y, int z) {
        return y;
    }
}
