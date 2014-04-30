package dk.au.cs.dash.test.dash.integer.simple.fail;

import dk.au.cs.dash.Dash;

public class AssignmentFail {
    public static int test(int i) {
        i = i + 1;
        Dash.error();
        return i;
    }
}
