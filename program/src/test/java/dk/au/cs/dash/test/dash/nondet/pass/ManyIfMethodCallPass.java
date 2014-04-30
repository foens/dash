package dk.au.cs.dash.test.dash.nondet.pass;

import dk.au.cs.dash.Dash;

public class ManyIfMethodCallPass {
    public static void test(int x) {
        if (Dash.nondet()) {
            if (Dash.nondet()) {

            } else {
                if (Dash.nondet()) {

                } else {
                    int k = 0;
                    if (Dash.nondet())
                        if (Dash.nondet())
                            k = foo(x);

                    if(k == 2)
                        Dash.error();
                }
            }
        }
    }

    public static int foo(int j) {
        if( j == 10) {
            if(Dash.nondet())
                return 6;
        }

        return -1;
    }
}
