package dk.au.cs.dash.test.dash.bool.simple.fail;

import dk.au.cs.dash.Dash;

public class BoolVariableFail {

    public static void test() {
        boolean b = true;
        Dash.error();
    }
}
