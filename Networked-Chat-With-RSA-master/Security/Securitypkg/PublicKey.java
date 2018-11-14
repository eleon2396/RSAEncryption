/*
 * 
 *  Class that will act as a tuple that will represent a public
 *  key generated by the RSA algorithm
 *  
 */
package Securitypkg;
import java.math.BigInteger;

public class PublicKey
{
	//  key, val pair for encryption
	private BigInteger n;
	private BigInteger e;
	
	// Constructor
	// @Param - n = computed by the the given prime numbers  p and q
	// @Param - e = computed by finding the GCD of p and q
	public PublicKey(BigInteger n, BigInteger e)
	{
		this.n = n;
		this.e = e;
	}
	
	// getter
	public BigInteger getN()
	{
		return n;
	}
	
	// getter
	public BigInteger getE()
	{
		return e;
	}
}