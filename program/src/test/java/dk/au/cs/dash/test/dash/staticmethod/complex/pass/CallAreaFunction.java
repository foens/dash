package dk.au.cs.dash.test.dash.staticmethod.complex.pass;

import dk.au.cs.dash.Dash;

public class CallAreaFunction {
    public static void test(int start, int end) {

        int total = area(start, end);

        if(start > 0 && start < 10 && end < 10 && end > 0 && total == -1)
            Dash.error();
    }

    private static int area(int start, int end) {
        return (end - start) * start + (end - start) * (end - start)/2;
    }

}
