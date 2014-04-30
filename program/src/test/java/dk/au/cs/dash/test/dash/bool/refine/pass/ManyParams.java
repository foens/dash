package dk.au.cs.dash.test.dash.bool.refine.pass;

import dk.au.cs.dash.Dash;

public class ManyParams {

    public static void test(boolean b1, boolean b2, boolean b3) {

        boolean c1, c2, c3;


        if(b2 && b3)
            c2 = true;
        else
            c2 = false;

        if(b3)
            c3 = true;
        else
            c3 = false;

        if( c2 && !b3)
            Dash.error();
    }
}
