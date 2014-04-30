package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

public class ModuloPass {
    public static void test(int i) {
        int j = i % 2;
        if(j == 0)
            if(i == 7)
                Dash.error();
    }
}
