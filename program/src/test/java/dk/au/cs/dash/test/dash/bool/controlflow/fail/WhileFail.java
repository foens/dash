package dk.au.cs.dash.test.dash.bool.controlflow.fail;

import dk.au.cs.dash.Dash;

public class WhileFail {

    public static void test() {
        boolean b = false;
        while (b) {

        }

        Dash.error();
    }
}
