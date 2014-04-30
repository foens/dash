package dk.au.cs.dash.test.dash.staticmethod.complex.pass;

import dk.au.cs.dash.Dash;

public class SimplifiedCallMultipleTimesInsideWhilePass {
    public static void test(int x, int g) {
        if( x < 0)
            x = neg(x);

        while(g > 3)
        {
            g--;
            x = neg(x);
        }
        if(x == -10)
            Dash.error();
    }

    private static int neg(int z) {
        return -z;
    }
}
