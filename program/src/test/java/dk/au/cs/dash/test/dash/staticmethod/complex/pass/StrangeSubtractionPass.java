package dk.au.cs.dash.test.dash.staticmethod.complex.pass;

import dk.au.cs.dash.Dash;

public class StrangeSubtractionPass {
    public static void test(int x) {
        int z = xMinusThreeX(x);
        if(z == 1) // Cannot find input where x = x - 3x is 1
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
