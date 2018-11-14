package Networkpkg;


import java.io.*;
import java.math.BigInteger;
import java.util.Vector;
//Class for the name and key pair that will hold the clients name and key
public class NameAndKeyPair implements Serializable
{
	//variables for the public key of the clients and names
	private Vector<BigInteger> publicKey;
	private String name;
	
	//initialize the clients specific public key and name
	public NameAndKeyPair(Vector<BigInteger> pubKey, String name)
	{
		this.name = name;
		this.publicKey = pubKey;
	}
	
	//return a vector of the public keys
	public Vector<BigInteger> getPubKey()
	{
		return publicKey;
	}
	
	//return the name of the client
	public String getName()
	{
		return name;
	}
}
