package dk.au.cs.dash.test.dash.bool.simple.pass;

public class ExprHalfAdder {
    //http://en.wikipedia.org/wiki/Exclusive_or
    public static boolean test(boolean p, boolean q) {
        boolean left = p || q;
        boolean right = !(p && q);
        return left && right;
    }
}
