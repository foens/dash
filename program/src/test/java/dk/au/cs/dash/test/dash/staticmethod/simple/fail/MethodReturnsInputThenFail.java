package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class MethodReturnsInputThenFail {

    public static void test(int a) {
        int x = returnInput(a);
        if(x == 3)
            Dash.error();
    }

    private static int returnInput(int b) {
        return b;
    }
}
