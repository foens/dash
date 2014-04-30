package dk.au.cs.dash.test.dash.examples;

import dk.au.cs.dash.Dash;

public class DashFigure3 {
    public void alias(IntObject p, IntObject p1, IntObject p2)
    {
        if(p == p1) return;
        if(p == p2) return;
        p1.v = 0; p2.v = 0;
        p.v = 1;
        if(p1.v == 1 || p2.v == 1)
            Dash.error();
        p = p1;
        p = p2;
    }

    private static class IntObject {
        public int v;
    }
}
