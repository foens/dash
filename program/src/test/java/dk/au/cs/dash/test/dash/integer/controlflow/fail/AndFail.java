package dk.au.cs.dash.test.dash.integer.controlflow.fail;

import dk.au.cs.dash.Dash;

public class AndFail {
    public static int test(int i) {
        i = i & 3;
        if (i == 2)
            Dash.error();
        return i;
    }
}
