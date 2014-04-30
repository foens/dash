package dk.au.cs.dash.test.dash.integer.refine.pass;

import dk.au.cs.dash.Dash;

public class PrimePass {
    public static void test(int possiblePrime) {
        // Fails if a prime is given in the range 12-16, however, 13 is excluded which is the only prime
        if(possiblePrime > 11 && possiblePrime < 17 && possiblePrime != 13)
        {
            for (int i = 2; i < possiblePrime; i++)
                if (possiblePrime % i == 0)
                    return;

            Dash.error();
        }
    }
}
