package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

public class ModuloCheckPass {
    public static void test(int i) {
        int j = i % 2;
        if(j == 0)
        {
            int z = 0;
            while(i > 0)
            {
                z++;
                i--;
            }
            if(z % 2 != 0)
                Dash.error();
        }
    }
}
