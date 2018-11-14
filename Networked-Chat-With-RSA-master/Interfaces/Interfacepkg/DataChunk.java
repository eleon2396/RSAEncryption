//
// This class represents the block of messages that is being sent to Server from Client
// Vector data structure is used for safe threading
//

package Interfacepkg;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Vector;

import Networkpkg.NameAndKeyPair;

@SuppressWarnings("serial")
public class DataChunk implements Serializable
{
	// data that both gets sent to both server and client
	private String sender;
	
	// data that gets sent to the server to process
	private Vector<BigInteger> encondedMessages;
	private NameAndKeyPair name;
	
	// data that gets sent to client to process
	private Vector<BigInteger> sentMsg;
	
	public DataChunk(String sender, Vector<BigInteger> encondedMessages, NameAndKeyPair names)
	{
		this.sender = sender;
		this.encondedMessages = encondedMessages;
		this.name = names;
		
		sentMsg = null;
	}
	
	public DataChunk(String sender, Vector<BigInteger> sentMsg)
	{
		this.sender = sender;
		this.sentMsg = sentMsg;
	}
	
	
	public String getSender() 
	{
		return sender;
	}

	public Vector<BigInteger> getEncondedMessage() {
		return encondedMessages;
	}
	
	public NameAndKeyPair getNames()
	{
		return name;
	}
	
	public Vector<BigInteger> getSentMsg()
	{
		return sentMsg;
	}

}
