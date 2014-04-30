package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

/**
 * Created by Jacob on 10-03-14.
 */
public class MultiplyNumberEqualToPrimePass {

    public static void test(int x, int y) {

        if(x > 1000 || y > 1000)
            return;

        if(x > 1 && y > 1 && x * y == 941)
            Dash.error();
    }
}
