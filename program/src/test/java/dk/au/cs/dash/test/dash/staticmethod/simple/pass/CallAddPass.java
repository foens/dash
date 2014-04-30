package dk.au.cs.dash.test.dash.staticmethod.simple.pass;

import dk.au.cs.dash.Dash;

public class CallAddPass {
    public static void test(int x, int y)
    {
        if(x > 0)
        {
            y = 4;
            int q = sum(x, y);
            if(q == 5)
                if(x == 2)
                    Dash.error();
        }
    }

    private static int sum(int i, int x)
    {
        int s = i + x;
        return s;
    }
}
