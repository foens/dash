package dk.au.cs.dash.test.dash.bool.refine.fail;

import dk.au.cs.dash.Dash;

public class EmptyIfFail {
    public static void test(boolean b) {
        if(!b)
            b = b;
        if(b)
            Dash.error();
    }
}