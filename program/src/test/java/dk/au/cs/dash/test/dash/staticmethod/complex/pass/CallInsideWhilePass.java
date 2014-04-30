package dk.au.cs.dash.test.dash.staticmethod.complex.pass;

import dk.au.cs.dash.Dash;

public class CallInsideWhilePass {
    public static void test(boolean b, int x) {
        int y = 0;
        while(b)
        {
            y = foo(x);
            b = false;
        }
        if(y == -10 && x != -15)
            Dash.error();
    }

    private static int foo(int z) {
        return z + 5;
    }
}
