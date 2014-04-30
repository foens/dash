package dk.au.cs.dash.test.dash.bool.refine.fail;

import dk.au.cs.dash.Dash;

public class WhileCountWithBoolean4Fail {
    public static void test(boolean p) {

        boolean b1 = false;
        boolean b2 = false;
        while (!(b1 && b2)) {

            if (!b1 && b2 && p)
                Dash.error();

            boolean carry = b1;
            b1 = b1 ^ true;
            b2 = b2 ^ carry;

        }
    }
}
