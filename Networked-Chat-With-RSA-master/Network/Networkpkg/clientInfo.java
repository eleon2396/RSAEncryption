package Networkpkg;

import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Vector;
//This class is for the clientinfo where the information of the client is saved like the output stream and the name and key
public class clientInfo 
{
	public ObjectOutputStream outputClient;
	private NameAndKeyPair nameAndKey;
	
	//initialize each client once it is made
	public clientInfo(ObjectOutputStream outputClient, NameAndKeyPair nameAndKey)
	{
		this.outputClient = outputClient;
		this.nameAndKey = nameAndKey;

		
	}
	
	//return the object output stream of the client
	public ObjectOutputStream getOBOS() {
		return outputClient;
	}
	
	//return the name and key pair of the client
	public NameAndKeyPair getNameNKey() {
		return nameAndKey;
	}
	
	

}	
