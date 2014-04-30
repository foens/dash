package dk.au.cs.dash.test.dash.staticmethod.complex.pass;

import dk.au.cs.dash.Dash;

public class IsPrimePass {
    public static void test(int possiblePrime) {
        if(possiblePrime > 7 && possiblePrime < 11 && isPrime(possiblePrime))
            Dash.error();
    }

    private static boolean isPrime(int possiblePrime) {
        for (int i = 2; i < possiblePrime; i++)
            if (possiblePrime % i == 0)
                return false;
        return true;
    }
}
