package dk.au.cs.dash.test.dash.integer.refine.fail;

import dk.au.cs.dash.Dash;

public class PrimeFail {
    public static void test(int possiblePrime) {
        // Fails if given a prime above 11
        if(possiblePrime > 11)
        {
            for (int i = 2; i < possiblePrime; i++)
                if (possiblePrime % i == 0)
                    return;

            Dash.error();
        }
    }
}
