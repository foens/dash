package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

public class AndPass {
    public static int test(int i) {
        i = i & 4;
        if (i == 2)
            Dash.error();
        return i;
    }
}