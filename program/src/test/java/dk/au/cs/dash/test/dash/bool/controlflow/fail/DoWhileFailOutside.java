package dk.au.cs.dash.test.dash.bool.controlflow.fail;

import dk.au.cs.dash.Dash;

public class DoWhileFailOutside {
    public static void test() {
        boolean b = true;
        do {
            b = false;
        } while (b);
        Dash.error();
    }
}
