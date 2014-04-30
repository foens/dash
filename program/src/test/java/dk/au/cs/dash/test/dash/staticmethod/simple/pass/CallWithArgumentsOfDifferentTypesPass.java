package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallWithArgumentsOfDifferentTypesPass {
    public static void test(int i, boolean k) {
        int z = foo(i, k);
        if (z == 1)
            Dash.error();
    }

    private static int foo(int i, boolean k) {
        if(k)
        {
            if(i > 5)
                return 0;
            return 3;
        }
        return 2;
    }
}
