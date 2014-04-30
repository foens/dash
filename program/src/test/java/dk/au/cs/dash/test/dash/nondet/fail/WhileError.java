package dk.au.cs.dash.test.dash.nondet.fail;

import dk.au.cs.dash.Dash;

public class WhileError {
    public static void test() {
        boolean b = true;
        boolean c = true;
        while(b) {
            if(Dash.nondet())
                c = false;

            b = false;
        }

        if(!c)
            Dash.error();
    }
}
