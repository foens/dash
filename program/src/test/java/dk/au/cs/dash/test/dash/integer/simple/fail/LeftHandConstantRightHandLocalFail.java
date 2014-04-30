package dk.au.cs.dash.test.dash.integer.simple.fail;

import dk.au.cs.dash.Dash;

public class LeftHandConstantRightHandLocalFail {
    public static int test(int x) {
        if (2*x == x + 10) // notice 2*x where x is on the right and not on the left
                Dash.error();
        return 0;
    }
}