package dk.au.cs.dash.test.dash.staticmethod.recursive.pass;

import dk.au.cs.dash.Dash;

public class Fib0ConstantPass {
    public static void test() {
        int fib0 = fib(0);
        if(fib0 == 1)
            Dash.error();
    }

    private static int fib(int i)
    {
        if (i == 0) return 0;
        if (i <= 2) return 1;

        return fib(i - 1) + fib(i - 2);
    }
}
