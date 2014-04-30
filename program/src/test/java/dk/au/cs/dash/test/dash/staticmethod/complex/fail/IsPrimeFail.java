package dk.au.cs.dash.test.dash.staticmethod.complex.fail;

import dk.au.cs.dash.Dash;

public class IsPrimeFail {
    public static void test(int possiblePrime) {
        if(possiblePrime > 11 && isPrime(possiblePrime))
            Dash.error();
    }

    private static boolean isPrime(int possiblePrime) {
        for (int i = 2; i < possiblePrime; i++)
            if (possiblePrime % i == 0)
                return false;
        return true;
    }
}
