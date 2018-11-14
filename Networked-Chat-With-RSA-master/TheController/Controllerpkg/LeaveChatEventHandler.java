// event handler for leaveChat button
// uses Chat instance to call Client function
// close connection using instance of a client in chat

package Controllerpkg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Interfacepkg.Chat;
import Interfacepkg.MessageBox;

public class LeaveChatEventHandler  implements ActionListener 
{
	// references of chat gui
	private Chat ref;
	
	// constructor
	// get the reference of the gui to be able to modify data members that only the 
	// gui has access to
	public LeaveChatEventHandler(Chat ref)
	{
		this.ref = ref;
	}
	
	// if user clicks leave chat then do this...
	@Override
	public void actionPerformed(ActionEvent event)
	{
		// close socket then clear the user list
		ref.callCloseConnection();
		ref.clearUserList();
		MessageBox.addMessage("You are disconnected from chat now");
	}
	
	
}
