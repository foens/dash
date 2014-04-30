package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

public class MoreAndPass {
    public static int test(int i) {
        int j = i & 0xFF00FF00;
        int q = i & 0x00FF00FF;
        int z = q & j;
        if (z != 0)
            Dash.error();
        return i;
    }
}
