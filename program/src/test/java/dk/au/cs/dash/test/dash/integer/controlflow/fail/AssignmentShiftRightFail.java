package dk.au.cs.dash.test.dash.integer.controlflow.fail;

import dk.au.cs.dash.Dash;

public class AssignmentShiftRightFail {
    public static int test(int i) {
        i >>= 1;

        if (i > 4)
            Dash.error();
        return i;
    }
}
