package dk.au.cs.dash.test.dash.integer.simple.fail;

import dk.au.cs.dash.Dash;

public class ExclusiveOrFail {
    public static int test(int i) {
        i = i ^ 4;
        Dash.error();
        return i;
    }
}
