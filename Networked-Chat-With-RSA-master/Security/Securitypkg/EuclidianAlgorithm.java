/*
 * 
 * This class finds the greatest common divisor using the euclidian algorithm
 * 
 */

package Securitypkg;

import java.math.BigInteger;

class EuclidianAlgorithm 
{
	// instances that are used to compute and
	// store the greatest common divisor
	private BigInteger GCD;
	private BigInteger prime1;
	private BigInteger prime2;
	
	// constructor
	// @Param - prime1 - first number given
	// @Param - prime2 - second number given
	public EuclidianAlgorithm(BigInteger prime1, BigInteger prime2)
	{
		this.prime1 = prime1;
		this.prime2 = prime2;
	}
	
	// set new numbers for the private instances
	// @Param - num1 - first int passed in
	// @Param - num2 - second int passed in
	public void setNewNumbers(BigInteger num1, BigInteger num2)
	{
		prime1 = num1;
		prime2 = num2;
	}
	
	// this is the public method that can be called to 
	// compute the gcd. checks which number is bigger and
	// calls the algorithm accordingly
	public void computeGCD()
	{
		if(prime1.compareTo(prime2) > 0)
			GCD = startAlgorithm(prime1, prime2);
		else
			GCD = startAlgorithm(prime2, prime1);
	}
	
	// getter
	public BigInteger getGCD()
	{
		return GCD;
	}
	
	// algorithm that will compute the greatest common divisor and return it
	// @Param - biggest - assumes the biggest integer is passed between the two parameters
	// @Param - smallest - assumes the smallest integer is passed between the two parameters
	private BigInteger startAlgorithm(BigInteger biggest, BigInteger smallest)
	{
		BigInteger remainder = biggest.mod(smallest);// % smallest;
		BigInteger prev = remainder;
		
		while(!remainder.equals(BigInteger.ZERO))
		{
			prev = remainder;
			
			biggest = smallest;
			smallest = remainder;
			
			remainder = biggest.mod(smallest);
		}
		
		
		return prev;
	}

}
