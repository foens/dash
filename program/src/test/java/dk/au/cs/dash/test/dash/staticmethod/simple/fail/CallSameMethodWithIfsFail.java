package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallSameMethodWithIfsFail {
    public static void test(int k) {
        boolean c = returnFalse(k == 3);
        boolean z = returnFalse(c);
        boolean q = returnFalse(!z);
        if (!q && k == 9)
            Dash.error();
    }

    private static boolean returnFalse(boolean b) {
        if(b)
            return !b;
        return b;
    }
}
