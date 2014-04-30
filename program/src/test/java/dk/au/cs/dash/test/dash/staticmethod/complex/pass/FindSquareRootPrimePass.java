package dk.au.cs.dash.test.dash.staticmethod.complex.pass;

import dk.au.cs.dash.Dash;

/**
 * Created by Jacob on 10-03-14.
 */
public class FindSquareRootPrimePass {

    public static void test(int x) {

        if(square(x) == 941)
            Dash.error();
    }

    private static int square(int x) {
        return x * x;
    }
}
