package dk.au.cs.dash.test.dash.integer.controlflow.fail;

import dk.au.cs.dash.Dash;

public class MultiplyFail {
    public static int test(int i) {
        i = i * 2;

        if (i == -4)
            Dash.error();
        return i;
    }
}
