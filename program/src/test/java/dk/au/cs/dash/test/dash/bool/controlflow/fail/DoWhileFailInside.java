package dk.au.cs.dash.test.dash.bool.controlflow.fail;

import dk.au.cs.dash.Dash;

public class DoWhileFailInside {
    public static void test() {
        boolean b = false;
        do {
            Dash.error();
        } while (b);
    }
}
