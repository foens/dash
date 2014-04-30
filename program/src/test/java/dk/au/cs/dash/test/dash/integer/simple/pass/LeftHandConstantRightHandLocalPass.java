package dk.au.cs.dash.test.dash.integer.simple.pass;

import dk.au.cs.dash.Dash;

public class LeftHandConstantRightHandLocalPass {
    public static int test(int x) {
        if (1+x == x) // notice 1+x where x is on the right and not on the left
                Dash.error();
        return 0;
    }
}