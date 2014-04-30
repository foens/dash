package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallWithManyArgumentsPass {
    public static void test(int i, int k, int j) {
        int z = foo(i, j, k);
        if (z == 2)
            Dash.error();
    }

    private static int foo(int i, int k, int j) {
        if (i == 0 && k == 1 && j == -3)
            return 0;
        return 1;
    }
}
