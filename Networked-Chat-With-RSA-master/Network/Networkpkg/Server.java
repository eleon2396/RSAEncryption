package Networkpkg;

import java.io.*;
import java.net.*;

import javax.swing.*;

import Interfacepkg.DataChunk;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

//This class is for the server code, this server will hold information of all the clients that join the server and display the information to the clients
//This server code will also have a GUI that we display once a client joins and when they leave
public class Server extends JFrame
{
	//variables for the server socket
	private ServerSocket server;
	//vector variable for the client list
	private Vector<clientInfo> clientList  = new Vector<clientInfo>();
	
	//GUI variables for the Server GUI which will have a history box, an iplabel and port label
	private JTextArea history;
	private JLabel IPLabel;
	private JLabel portLabel;
	private String host = null;
	private int port = 0;
	
	
	//constructor
	public Server() 
	{
		//start running the server and setup GUI
		startRunning();
		//setup container and layout
		Container container  = getContentPane();
		container.setLayout(new FlowLayout());
		
		//create new labels that will hold the information of which ip address and port to connect to
		IPLabel = new JLabel();
		portLabel = new JLabel();
		
		//try and catch to get the ip address and port
		try {
			host = InetAddress.getLocalHost().getHostAddress();
			port = server.getLocalPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("There was an error with getting the IP address or getting the port...");
		}
		
		//set the text for the ip label and port
		IPLabel.setText("Connect to: " + host);
		portLabel.setText("with Port: " + Integer.toString(port));
		
		//setup the histry box
		history  = new JTextArea(10,40);
		history.setEditable(false);
		
		//add objects to the container
		container.add(IPLabel);
		container.add(portLabel);
		container.add(new JScrollPane(history));
		container.setBackground(new Color(204, 255, 245));
		
		//set the size and visible
		setSize(500, 250);
		setVisible(true);
	}
	
	//server code to start the waitforconnection phase
	public void startRunning()
	{
		try
		{

			server = new ServerSocket(0); 
			
			//Trying to connect and have conversation
			 new waitForConnection();
			
		} catch (IOException ioException){
			System.out.println("There is an error with the server");
		}
	}

	//method to update users once they join
	public void updateUsersOnJoin(NameAndKeyPair newClient)
	{
		
		for(clientInfo currentClient : clientList)
		{
			ObjectOutputStream clientOut = currentClient.getOBOS();
			
			try {
				clientOut.writeObject(newClient);
				//history.append(currentClient.getNameNKey().getName() + " joined the server\n");
			} catch (IOException e) {

				System.err.println("sending new client info failed :c");
			}
		}
	}
	
	//method to update users once they leave
	public void updateUsersOnLeave(clientInfo leavingClient)
	{
		// make dummy NameAndKey that tell other clients to delete client
		NameAndKeyPair deletePair = new NameAndKeyPair(null, leavingClient.getNameNKey().getName());
		
		// first delete client from the server list
		clientList.remove(leavingClient);
		
		// then send delete to online users
		for(clientInfo clientInf : clientList)
		{
			ObjectOutputStream out = clientInf.getOBOS();
			try {
				out.writeObject(deletePair);
				//history.append(clientInf.getNameNKey().getName() + " left the server\n");
				out.flush();
			} catch (IOException e) {
				System.out.println("error sending delete client info");
			}
			
		}
	}
	
	//method to send the client list where it is needed
	public void sendClientList(ObjectOutputStream out, String clientName)
	{
		for(clientInfo cInfo : clientList)
		{
			NameAndKeyPair pair = cInfo.getNameNKey();
			if(!clientName.equals(pair.getName()))
			{
				try
				{
					out.writeObject(pair);
					out.flush();
				} 
				catch (IOException e) 
				{
					System.err.println("error trying to update user who joined!");
				}
			}
		}
	}
	
	//method to print all the users/clients
	public void printUsers() {
		
		for(clientInfo client : clientList) {
			NameAndKeyPair temp = client.getNameNKey();
			System.out.println("Client name: " +temp.getName());
		}
		
		
	}
	
	//private class to wait for a connection from the client
	private class waitForConnection implements Runnable
	{
		
		
		public waitForConnection()
		{
			new Thread(this).start();
		}
		
		public void run() 
		{
			while(true) 
			{
				try 
				{
					new communicationThread(server.accept());
					//printUsers();
				}
				catch(IOException e) 
				{
					System.out.println("There was an error with connecting to another client");
				}
			}
		}
	}
	
	
	//private class that will deal with the communication between a client and server and other clients
	private class communicationThread implements Runnable
	{
		private Socket connection;
		private boolean b = true;
		
		public communicationThread(Socket newClient) 
		{
			connection = newClient;
			new Thread(this).start();
		}
		
		//actual communication code for the client and server
		public void run() 
		{
			//setup streams
			ObjectOutputStream out = null;
			ObjectInputStream in = null;
			try 
			{
				//setup streams
				out = new ObjectOutputStream(connection.getOutputStream());
				in = new ObjectInputStream(connection.getInputStream());
				
				DataChunk clientInput;
				NameAndKeyPair clientInformation;
				
				//whenever a client is added to the server we send it to the actual server
				clientInformation = (NameAndKeyPair) in.readObject();
				
				// update the server's list and then update the clients' list
				clientList.addElement(new clientInfo(out, clientInformation));
				history.append(clientInformation.getName() + " joined the server\n");
				updateUsersOnJoin(clientInformation);
				sendClientList(out, clientInformation.getName());
				
				while(b)
				{
					clientInput = (DataChunk) in.readObject();
					
					NameAndKeyPair sendTo = clientInput.getNames();
					String userName = sendTo.getName();
					for(clientInfo inf : clientList)
					{
						if(userName.equals(inf.getNameNKey().getName()))
						{
							ObjectOutputStream sendeeOut = inf.getOBOS();
							sendeeOut.writeObject(clientInput);
						}
					}
					//System.out.println("Client: " + clientInput);
				}
				
				out.close();
				in.close();
				connection.close();
			} 
			catch (IOException | ClassNotFoundException e)
			{
				for(clientInfo clientInf : clientList)
				{
					ObjectOutputStream clientOut = clientInf.getOBOS();
					//NameAndKeyPair pair = clientInf.getNameNKey();
					if(clientOut.equals(out))
					{
						updateUsersOnLeave(clientInf);
						history.append(clientInf.getNameNKey().getName() + " left the server\n");
						break;
					}
				}
				
			}

			
		}
		
		
	}
	
	
}