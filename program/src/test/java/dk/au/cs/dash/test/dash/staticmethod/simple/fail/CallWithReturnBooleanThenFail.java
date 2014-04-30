package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallWithReturnBooleanThenFail {

    public static void test() {
        int i = 0;
        boolean x = giveMeTrue();
        if(x)
            Dash.error();
    }

    private static boolean giveMeTrue() {
        return true;
    }
}
