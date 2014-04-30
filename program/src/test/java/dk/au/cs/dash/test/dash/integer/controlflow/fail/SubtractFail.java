package dk.au.cs.dash.test.dash.integer.controlflow.fail;

import dk.au.cs.dash.Dash;

public class SubtractFail {
    public static int test(int i) {
        i = i - 1;

        if (i == 1)
            Dash.error();
        return i;
    }
}
