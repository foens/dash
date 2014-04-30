package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallWithReturnThenFail {

    public static void test() {
        int i = 0;
        int x = giveMeThree();
        if(x == 3)
            Dash.error();
    }

    private static int giveMeThree() {
        return 3;
    }
}
