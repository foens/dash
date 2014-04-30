package dk.au.cs.dash.test.dash.staticmethod.complex.fail;

import dk.au.cs.dash.Dash;

public class CallInLoopWithSumOverRange {
    public static void test(int start, int end) {

        int total = 0;
        for(int i = 0; i < 10; ++i) {
            if(end - start > 0) {
                total = sum(start, end);
                start++;
            } else {
                total = sum(end, start);
                start--;
            }
        }

        if(total == -1)
            Dash.error();
    }

    private static int sum(int start, int end) {
        return (end - start) * start + (end - start) * (end - start)/2;
    }

}
