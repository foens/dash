package dk.au.cs.dash.test.dash.integer.controlflow.fail;

import dk.au.cs.dash.Dash;

public class IntParamFailWithEqual {
    public static void test(int i) {
        if (i == 100)
            Dash.error();
    }
}
