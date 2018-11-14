/*
 *  This class handles the event if the user decides to click the send message button
 *  there are error checks in place where if the users the client wants to send a message to is empty
 *  then they cant send the message to the sever
 */
package Controllerpkg;
import Networkpkg.*;
import java.awt.event.*;
import java.util.Vector;

import Interfacepkg.*;


public class SendMessageHandler implements ActionListener
{
	// instance variables needed to reference certain objects
	private Chat ref;
	private Client client;
	
	// constructor
	// needs chat reference to access private variables
	public SendMessageHandler(Chat ref)
	{
		this.ref = ref;
	}
	
	// if user clicks send do this...
	@Override
	public void actionPerformed(ActionEvent event)
	{
		// get client object that chat gui contains
		client = ref.getClientSocket();
		
		// get the users the clients wants to send a message to
		Vector<DataChunk> recievers = ref.getClientsToSendMsg(); 
		
		// if the list is null then just return
		if (recievers == null)
		{
			return;
		}
		
		// send a datachunk for every person the client wants to send a message to
		for(DataChunk rec : recievers)
		{
			client.sendMessage(rec);
		}
		
	}

}