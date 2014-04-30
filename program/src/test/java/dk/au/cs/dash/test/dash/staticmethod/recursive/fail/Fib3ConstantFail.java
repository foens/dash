package dk.au.cs.dash.test.dash.staticmethod.recursive.fail;

import dk.au.cs.dash.Dash;

public class Fib3ConstantFail {
    public static void test() {
        int fib3 = fib(3);
        if(fib3 == 2) // fib3 is 2
            Dash.error();
    }

    private static int fib(int i)
    {
        if (i == 0) return 0;
        if (i <= 2) return 1;

        return fib(i - 1) + fib(i - 2);
    }
}
