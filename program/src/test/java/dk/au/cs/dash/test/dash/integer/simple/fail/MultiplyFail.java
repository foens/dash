package dk.au.cs.dash.test.dash.integer.simple.fail;

import dk.au.cs.dash.Dash;

public class MultiplyFail {
    public static int test(int i) {
        i = i * 2;
        Dash.error();
        return i;
    }
}
