package dk.au.cs.dash.test.dash.staticmethod.simple.fail;

import dk.au.cs.dash.Dash;

public class CallNestedMethodFail {

    public static void test(int i) {
        i = inc10(i);

        if(i == 50) {
            Dash.error();
        }
    }

    private static int inc10(int i) {
        return inc9(i) + 1;
    }

    private static int inc9(int i) {
        return inc8(i) + 1;
    }

    private static int inc8(int i) {
        return inc7(i) + 1;
    }

    private static int inc7(int i) {
        return inc6(i) + 1;
    }

    private static int inc6(int i) {
        return inc5(i) + 1;
    }

    private static int inc5(int i) {
        return inc4(i) + 1;
    }

    private static int inc4(int i) {
        return inc3(i) + 1;
    }

    private static int inc3(int i) {
        return inc2(i) + 1;
    }

    private static int inc2(int i) {
        return inc1(i) + 1;
    }

    private static int inc1(int i) {
        return i + 1;
    }


}
