package dk.au.cs.dash.test.dash.staticmethod.multiclass.fail.CallSumClass;

import dk.au.cs.dash.Dash;

public class CallSumClass {

    public static void test(int x, int y, int z) {
        int q = z * y;
        if(y != 0 && Sum.sum(x, y) == 17) {
            Dash.error();
        }
    }
}
