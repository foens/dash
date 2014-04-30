package dk.au.cs.dash.test.dash.nondet.fail;

import dk.au.cs.dash.Dash;

public class ManyIfError {
    public static void test() {
        if (Dash.nondet()) {
            if (Dash.nondet()) {

            } else {
                if (Dash.nondet()) {

                } else {
                    if (Dash.nondet())
                        if (Dash.nondet())
                            Dash.error();
                }
            }
        }
    }
}
