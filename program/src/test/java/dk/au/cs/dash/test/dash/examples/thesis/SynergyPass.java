package dk.au.cs.dash.test.dash.examples.thesis;

import dk.au.cs.dash.Dash;

public class SynergyPass {
    public static void test(int a) {
        int i = 0;
        while(i < 1000) {
            i = i + 1;
            if(a == 1)
                if(i == 1000)
                    Dash.error();
        }

    }
}
