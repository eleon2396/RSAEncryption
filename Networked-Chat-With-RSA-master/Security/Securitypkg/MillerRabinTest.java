/* This code is the implementation of the Miller-Rabin Test
 * 
 * NOTE: most of code was not written by group members, but was referenced from online sources
 *       references include: 
 *        - https://en.wikibooks.org/wiki/Algorithm_Implementation/Mathematics/Primality_Testing
 *        - http://hg.openjdk.java.net/jdk7/jdk7/jdk/file/00cd9dc3c2b5/src/share/classes/java/math/BigInteger.java#l2451
 *       
 */
package Securitypkg;

import java.math.BigInteger;
import java.util.Random;

class MillerRabinTest
{
	private static final BigInteger ZERO = BigInteger.ZERO;
	private static final BigInteger ONE = BigInteger.ONE;
	private static final BigInteger TWO = new BigInteger("2");
	private static final BigInteger THREE = new BigInteger("3");
	private boolean passedTest;
	
	public MillerRabinTest(BigInteger prime1, BigInteger prime2)
	{
		// check if both numbers are actually prime
		passedTest = (isProbablePrime(prime1, 5) && isProbablePrime(prime2, 5));
	}
	
	// getter method that determines if numbers passed to constructor were valid prime numbers
	public boolean isPrime()
	{
		return passedTest;
	}

	public static boolean isProbablePrime(BigInteger n, int k) {
		if (n.compareTo(ONE) == 0)
			return false;
		if (n.compareTo(THREE) < 0)
			return true;
		int s = 0;
		BigInteger d = n.subtract(ONE);
		while (d.mod(TWO).equals(ZERO)) {
			s++;
			d = d.divide(TWO);
		}
		for (int i = 0; i < k; i++) {
			BigInteger a = uniformRandom(TWO, n.subtract(ONE));
			BigInteger x = a.modPow(d, n);
			if (x.equals(ONE) || x.equals(n.subtract(ONE)))
				continue;
			int r = 0;
			for (; r < s; r++) {
				x = x.modPow(TWO, n);
				if (x.equals(ONE))
					return false;
				if (x.equals(n.subtract(ONE)))
					break;
			}
			if (r == s) // None of the steps made x equal n-1.
				return false;
		}
		return true;
	}

	private static BigInteger uniformRandom(BigInteger bottom, BigInteger top) {
		Random rnd = new Random();
		BigInteger res;
		do {
			res = new BigInteger(top.bitLength(), rnd);
		} while (res.compareTo(bottom) < 0 || res.compareTo(top) > 0);
		return res;
	}
}
