package dk.au.cs.dash.test.dash.examples;

import dk.au.cs.dash.Dash;

public class DashFigure6 {
    public static void test(int q)
    {
        int a, b;
        a = inc(q);
        b = inc(a);
        if(b != q+2)
            Dash.error();
    }

    private static int inc(int y)
    {
        int r;
        r = y+1;
        return r;
    }
}
