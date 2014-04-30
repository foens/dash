package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

public class SubtractPass {
    public static void test(int i) {
        int q = i;
        q -= i;
        q += 5;
        q -= 10;
        if(q != -5)
            Dash.error();
    }
}
