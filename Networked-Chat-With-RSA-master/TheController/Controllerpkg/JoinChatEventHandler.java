/*
 * This class handles the event for when the user clicks on the Join chat button. When the user presses the button join chat
 * They are connected to the server if it exists and are told if the connection was succesful or not
 */
package Controllerpkg;
import Networkpkg.*;
import java.awt.event.*;
import Interfacepkg.*;

public class JoinChatEventHandler implements ActionListener
{
	// instance variables
	private Chat ref;
	
	// constructor
	// gets the reference of the gui class
	// to be able to access data that the server class needs
	public JoinChatEventHandler(Chat ref)
	{
		this.ref = ref;
	}
	
	// if the button is clicked do this..
	@Override
	public void actionPerformed(ActionEvent event)
	{
		
		// create a new client object with the user's input
		Client c = new Client(ref.getIPInfo(), ref.getPortInfo(), ref);
		
		// check if the user's info gave a successful connection or not
		if(c.checkConnectionStatus())
		{
			MessageBox.addMessage("Conenction established :D");
			ref.allowAccess(c);
		}
		else
		{
			MessageBox.addMessage("Connection not established :c");
		}
	}

}
