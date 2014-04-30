package dk.au.cs.dash.test.dash.integer.controlflow.fail;

import dk.au.cs.dash.Dash;

public class UnaryDecFail {
    public static int test(int i) {
        i = --i;

        if (i >= 2)
            Dash.error();
        return i;
    }
}
