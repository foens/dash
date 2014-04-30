package dk.au.cs.dash.test.dash.integer.refine.fail;

import dk.au.cs.dash.Dash;


public class SubtleAbsOverflowFail {

    public static int test(int a) {
        if(a < 0)
            a = -a;

        if(a < 0)
            Dash.error();

        return a;
    }
}
