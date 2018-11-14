package Interfacepkg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;


/* 
 * Client list has an updated list of all
 * users connected to chat. Only clients that are currently connected
 * will be shown. Since list is same for all users, Singleton design is used
 */ 

public class ClientList {
	
	private static ClientList clientlist = null;
	private static JPanel clientPanel = null;
	private JList<String> list; // allows select option
	private static Vector<String> currentList = new Vector<String>();
	//thread safe container
	
	private ClientList(){
		clientPanel = new JPanel();
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);		
		JScrollPane panel = new JScrollPane(list);
		panel.setLayout(new ScrollPaneLayout()); //in case client list overlaps panel size
		panel.setBorder(BorderFactory.createTitledBorder("Clients Online:"));
		panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.setPreferredSize(new Dimension(280, 390));
		clientPanel.add(panel, BorderLayout.CENTER);
	}

	// receives call from Chat to clear clientList instance
	// when user is trying to reenter chat in the same session
	public void emptyClientList()
	{
		currentList.clear();
		list.setListData(currentList);
	}
	
	// update list in GUI whenever new user is joined
	public void addNewClient(String user)
	{
		currentList.addElement(user);
		list.setListData(currentList);
	}
	
	//update clientList when user is disconnected from chat
	public void removeClient(String user)
	{
		currentList.remove(user);
		list.setListData(currentList);
	}
	
	// create a vector of clients selected for sending message to
	public List<String> getChosenClients()
	{
		return list.getSelectedValuesList();
	}
	
	//singleton design for constructor, used for GUI right side panel 
	public static JPanel getClientList()
	{

	      if(clientlist == null)
	      {
	         clientlist = new ClientList();
	      }
	      return clientPanel;
	}
	
	//used client list modification
	public static ClientList getClientBox()
	{
		if(clientlist == null)
			clientlist = new ClientList();
		
		return clientlist;
	}
}