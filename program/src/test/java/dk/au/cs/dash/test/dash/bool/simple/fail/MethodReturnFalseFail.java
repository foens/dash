package dk.au.cs.dash.test.dash.bool.simple.fail;

import dk.au.cs.dash.Dash;

public class MethodReturnFalseFail {
    public static boolean test() {
        Dash.error();
        return false;
    }
}
