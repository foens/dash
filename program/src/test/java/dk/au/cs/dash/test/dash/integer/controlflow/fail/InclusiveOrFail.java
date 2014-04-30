package dk.au.cs.dash.test.dash.integer.controlflow.fail;

import dk.au.cs.dash.Dash;

public class InclusiveOrFail {
    public static int test(int i) {
        i = i | 4;

        if (i == 4 + 1)
            Dash.error();
        return i;
    }
}
