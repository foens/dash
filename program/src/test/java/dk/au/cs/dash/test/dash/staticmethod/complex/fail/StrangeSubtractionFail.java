package dk.au.cs.dash.test.dash.staticmethod.complex.fail;

import dk.au.cs.dash.Dash;

public class StrangeSubtractionFail {
    public static void test(int x) {
        if(x <= 0)
            return;

        int z = xMinusThreeX(x);
        if(z == -10) // x = 5 -> z = -10
            Dash.error();
    }

    private static int xMinusThreeX(int x) {
        int y = 3*x;
        while(y>0)
        {
            x--;
            y--;
        }
        return x;
    }
}
