package dk.au.cs.dash.test.dash.examples;

import dk.au.cs.dash.Dash;

public class SynergyFigure1 {
    public static void test(int a) {
        int i, c;
        i = 0;
        c = 0;
        while(i < 1000) {
            c = c + i;
            i = i + 1;
        }

        if(a < 0) {
            Dash.error();
        }
    }
}
