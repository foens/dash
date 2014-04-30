package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

public class NegatePass {
    public static void test(int i) {
        if(i > 0)
        {
            int j = -i;
            if(j > 0)
                Dash.error();
        }
    }
}
