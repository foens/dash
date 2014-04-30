package dk.au.cs.dash.test.dash.nondet.pass;

import dk.au.cs.dash.Dash;

public class DashFigure1Simple {

    public static void test()
    {
        boolean lock = false;
        int x = 0;
        int y = 0;

        do {
            if(lock)
                Dash.error();
            lock = true;

            x = y;
            if(Dash.nondet()) {
                y++;
                if(!lock)
                    Dash.error();
                lock = false; // unlock
            }
        } while(x != y);

        if(!lock)
            Dash.error();
    }
}
