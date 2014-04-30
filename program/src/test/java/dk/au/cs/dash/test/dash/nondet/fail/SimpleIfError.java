package dk.au.cs.dash.test.dash.nondet.fail;

import dk.au.cs.dash.Dash;

public class SimpleIfError {

    public static void test() {
        if(Dash.nondet()) {
            Dash.error();
        }
    }
}
