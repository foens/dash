package dk.au.cs.dash.test.dash.integer.simple.fail;

import dk.au.cs.dash.Dash;

public class BitwiseNegationFail {
    public static int test(int i) {
        i = ~i;
        Dash.error();
        return i;
    }
}