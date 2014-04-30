package dk.au.cs.dash.test.dash.examples;

import dk.au.cs.dash.Dash;

public class SynergyFigure1Pass {
    public static void test(int b) {
        int i, c;
        i = 0;
        c = 0;
        int a = 10000000;
        while(i < 1000) {
            c = c + i;
            i = i + 1;
        }

        if(a < 0) {
            Dash.error();
        }
    }
}
