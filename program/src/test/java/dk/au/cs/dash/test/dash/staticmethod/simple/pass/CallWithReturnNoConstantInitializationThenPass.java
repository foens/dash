package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallWithReturnNoConstantInitializationThenPass {

    public static void test() {
        int x = giveMeThree();
        if(x == 2)
            Dash.error();
    }

    private static int giveMeThree() {
        return 3;
    }
}
