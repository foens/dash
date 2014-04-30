package dk.au.cs.dash.test.dash.staticmethod.multiclass.pass.CallManyClass;

import dk.au.cs.dash.Dash;

/**
 * Created by Jacob on 18-03-14.
 */
public class CallManyClass {
    public static void test(int x, int y) {
        int q;
        if(x > 0)
            q = Call2.foo(y, x);
        else
            q = Call3.foo(x, y);

        if(q == 100) {
            Dash.error();
        }
    }

    public static int foo(int i) {
        return i + 1;
    }
}
