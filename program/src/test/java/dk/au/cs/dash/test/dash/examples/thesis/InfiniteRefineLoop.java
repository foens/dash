package dk.au.cs.dash.test.dash.examples.thesis;

import dk.au.cs.dash.Dash;

@SuppressWarnings({"StatementWithEmptyBody", "ConstantConditions"})
public class InfiniteRefineLoop {
    public static void test() {
        boolean b = false;
        for (int i = 0; i == 0; i++) { /* Empty */ }
        if (b)
            Dash.error();
    }
}
