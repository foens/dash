package dk.au.cs.dash.test.dash.examples;

import dk.au.cs.dash.Dash;

public class DartPage214 {
    public static int test(int x, int y) {
        if (x != y)
            if (f(x) == x + 10)
                Dash.error();
        return 0;
    }

    public static int f(int x) {
        return 2 * x;
    }
}
