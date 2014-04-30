package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallWithReturnThenPass {

    public static void test() {
        int x = 2000000000;
        x = giveMeThree();
        if(x == 2)
            Dash.error();
    }

    private static int giveMeThree() {
        return 3;
    }
}
