package dk.au.cs.dash.test.dash.bool.controlflow.fail;

import dk.au.cs.dash.Dash;

public class ForFail {

    public static void test() {
        boolean b = false;

        for (; b; b = true) {

        }
        Dash.error();
    }
}
