package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallThenFail {

    public static void test() {
        int i = 0;
        doNothing();
        Dash.error();
    }

    private static void doNothing() {

    }
}
