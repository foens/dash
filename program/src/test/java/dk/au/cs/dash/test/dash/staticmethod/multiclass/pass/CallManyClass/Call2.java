package dk.au.cs.dash.test.dash.staticmethod.multiclass.pass.CallManyClass;

/**
 * Created by Jacob on 18-03-14.
 */
public class Call2 {
    public static int foo(int y, int x) {
        if(x < 0 && x * y > 0) {
            return Call3.foo(4, 43);
        } else {
            return CallManyClass.foo(2);
        }
    }
}
