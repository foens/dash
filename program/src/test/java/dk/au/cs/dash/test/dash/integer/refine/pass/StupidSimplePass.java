package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

public class StupidSimplePass {
    public static void test(int x)
    {
        if(x < Integer.MIN_VALUE)
            Dash.error();
    }
}
