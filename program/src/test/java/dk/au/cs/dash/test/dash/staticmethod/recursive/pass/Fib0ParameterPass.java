package dk.au.cs.dash.test.dash.staticmethod.recursive.pass;

import dk.au.cs.dash.Dash;

public class Fib0ParameterPass {
    public static void test(int x) {
        int fib0 = fib(x);
        if(fib0 == 1)
            if(x == 0)
                Dash.error();
    }

    private static int fib(int i)
    {
        if (i <= 0) return 0;
        if (i <= 2) return 1;

        return fib(i - 1) + fib(i - 2);
    }
}
