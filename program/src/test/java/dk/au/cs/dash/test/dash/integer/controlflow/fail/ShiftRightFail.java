package dk.au.cs.dash.test.dash.integer.controlflow.fail;

import dk.au.cs.dash.Dash;

public class ShiftRightFail {
    public static int test(int i) {
        i = i >> 1;

        if (i != 0)
            Dash.error();

        return i;
    }
}