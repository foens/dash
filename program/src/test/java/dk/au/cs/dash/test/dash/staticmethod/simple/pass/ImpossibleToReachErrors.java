package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

/**
 * Created by Jacob on 11-03-14.
 */
public class ImpossibleToReachErrors {


    public static void test(int x) {

        int y = x * 2;
        int z = foo(y);
        if(z == 0) {
            if(z - 10 == 10) {
                Dash.error();
            }
        } else {
            if(z * 2 == 1) {
                Dash.error();
            }
        }
    }

    public static int foo(int x) {
        return -x;
    }
}
