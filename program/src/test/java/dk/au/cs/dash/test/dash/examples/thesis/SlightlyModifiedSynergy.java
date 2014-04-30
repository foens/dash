package dk.au.cs.dash.test.dash.examples.thesis;

import dk.au.cs.dash.Dash;

public class SlightlyModifiedSynergy {
    public static void test(int a) {
        int i = 0;
        while( i < 1 ) {
            i = i + 1;
        }
        if(a < 0)
            Dash.error();
    }
}
