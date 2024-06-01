package Uebung1;

//import aud.*;

public class TailRecursion {
    public static int pow2(int n) {
        return _pow2(n, 1);
    }

    private static int _pow2(int exponent, int result) {
        if (exponent == 0) {
            return result;
        } else {
            return _pow2(exponent - 1, result * 2);
        }
    }

    	
    public static int sumFactors(int n) {
        return sumFactorsHelper(n, 1, 0);
    }

    private static int sumFactorsHelper(int n, int currentDivisor, int sum) {
        if (currentDivisor >= n) {
            return sum;
        } else {
            if (n % currentDivisor == 0) {
                return sumFactorsHelper(n, currentDivisor + 1, sum + currentDivisor);
            } else {
                return sumFactorsHelper(n, currentDivisor + 1, sum);
            }
        }
    }
}
