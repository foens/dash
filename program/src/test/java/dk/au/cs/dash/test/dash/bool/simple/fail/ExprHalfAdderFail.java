package dk.au.cs.dash.test.dash.bool.simple.fail;

import dk.au.cs.dash.Dash;

public class ExprHalfAdderFail {
    //http://en.wikipedia.org/wiki/Exclusive_or
    public static boolean test(boolean p, boolean q) {
        boolean left = p || q;
        boolean right = !(p && q);
        Dash.error();
        return left && right;
    }
}
