package UnitTests;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;
import Securitypkg.*;

public class RSATesting {

	@Test
	public void checkRSA() 
	{
		RSA r = new RSA(BigInteger.valueOf(11),BigInteger.valueOf(13));
		
		assertEquals(BigInteger.valueOf(11),r.getPVal());
		assertEquals(BigInteger.valueOf(13), r.getQVal());
		assertEquals(BigInteger.valueOf(143), r.getNVal());
		assertEquals(BigInteger.valueOf(120), r.getMVal());
		assertEquals(BigInteger.valueOf(7), r.getE());
		assertEquals(BigInteger.valueOf(103), r.getDVal());
	}

	@Test
	public void checkEuclidAlgo()
	{
		
		BigInteger gcd1 = RSA.getGCD(BigInteger.valueOf(10), BigInteger.valueOf(45));
		BigInteger gcd2 = RSA.getGCD(BigInteger.valueOf(1701), BigInteger.valueOf(3768));
		BigInteger gcd3 = RSA.getGCD(BigInteger.valueOf(5), BigInteger.valueOf(3));
		BigInteger gcd4 = RSA.getGCD(BigInteger.valueOf(108), BigInteger.valueOf(5));

		
		assertEquals(BigInteger.valueOf(5), gcd1);
		assertEquals(BigInteger.valueOf(3), gcd2);
		assertEquals(BigInteger.valueOf(1), gcd3);
		assertEquals(BigInteger.valueOf(1), gcd4);
	}
	
	@Test
	public void checkGCDAlgo()
	{
		RSA r = new RSA(BigInteger.valueOf(7),BigInteger.valueOf(19));
		BigInteger e1 = r.getE();
		
		assertEquals(BigInteger.valueOf(108), r.getMVal());
		assertEquals(BigInteger.valueOf(5), e1);
		assertEquals(BigInteger.valueOf(65), r.getDVal());
		
	}
}
