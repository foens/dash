package dk.au.cs.dash.test.dash.staticmethod.complex.pass;

import dk.au.cs.dash.Dash;

public class CallMultipleTimesInsideWhilePass {
    public static void test(int x, int g) {
        if( x < 0)
            x = neg(x);

        int startG = g;
        while(g > 3)
        {
            g--;
            x = neg(x);
        }
        if(x == -10 && (startG % 2) == 1)
            Dash.error();
    }

    private static int neg(int z) {
        return -z;
    }
}
