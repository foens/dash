package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallWithReturnBooleanThenPass {

    public static void test() {
        boolean x = true;
        x = giveMeFalse();
        if(x)
            Dash.error();
    }

    private static boolean giveMeFalse() {
        return false;
    }
}
