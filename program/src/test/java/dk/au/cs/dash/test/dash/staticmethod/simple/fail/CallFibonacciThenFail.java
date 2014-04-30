package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallFibonacciThenFail {
    public static void test(int k) {
        int i = fib(3);
        if(k > 4) {
            Dash.error();
        }
        i++;            //Force fib to be an assignWithCallOp
    }

    private static int fib(int i) {
        if(i < 1)
            return 0;
        else if(i == 1)
            return 1;
        else
            return fib(i - 1) + fib(i - 2);
    }
}
