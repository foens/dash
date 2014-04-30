package dk.au.cs.dash.test.dash.staticmethod.multiclass.pass.CallManyClass;

/**
 * Created by Jacob on 18-03-14.
 */
public class Call3 {
    public static int foo(int x, int y) {
        return Call2.foo(-x + 10, 15);
    }
}
