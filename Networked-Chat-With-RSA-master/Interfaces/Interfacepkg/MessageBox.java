/*
 * This is the JPanel in the gui for the messages the client will recieve and send.
 * This will also let the user know if a connection was made or if the user successfully disconnected
 * 
 */
package Interfacepkg;
  
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class MessageBox extends JPanel
{
	// declared static variables that will have previous messages show for user
	static JTextArea messageHistory;
	
	// constructor
	public MessageBox()
	{
		// create it for jpanel
		messageHistory = new JTextArea(20, 30);
		messageHistory.setEditable(false);
		// add to jpanel and change color of the background
		this.setBackground(new Color(204, 255, 245));
		this.add(new JScrollPane(messageHistory));
	}
	
	// this static method appends a message to the message box based on the
	// parameter passed in
	public static void addMessage(String msg)
	{
		messageHistory.append(msg + "\n");
	}
	
}
