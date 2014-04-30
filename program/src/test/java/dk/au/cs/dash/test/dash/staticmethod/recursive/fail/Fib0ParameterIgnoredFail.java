package dk.au.cs.dash.test.dash.staticmethod.recursive.fail;

import dk.au.cs.dash.Dash;

public class Fib0ParameterIgnoredFail {
    public static void test(int x, int y) {
        int fib0 = fib(x);
        if(y == 3)
            Dash.error();
        if(fib0 == 1)
            fib0 = 3;
    }

    private static int fib(int i)
    {
        if (i <= 0) return 0;
        if (i <= 2) return 1;

        return fib(i - 1) + fib(i - 2);
    }
}
