package dk.au.cs.dash.test.dash.integer.controlflow.fail;

import dk.au.cs.dash.Dash;

public class ForFail10 {
    public static int test(int i) {
        for (int j = 0; j < 10; ++j) {
            i = i;
        }
        if (i == 10)
            Dash.error();
        return i;
    }
}
