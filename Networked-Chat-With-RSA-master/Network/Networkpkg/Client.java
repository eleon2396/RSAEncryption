package Networkpkg;
import Interfacepkg.Chat;
import Interfacepkg.DataChunk;
import Securitypkg.*;
import java.io.*;
import java.net.*;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//This class will be in charge of the client code which includes connecting to a server and being able to send messages back and forth between the clients
public class Client extends JFrame
{
	//variables for the ouput and input streams
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	//variables for the host and port number
	private String host;
	private String port;
	
	//variables for the socket, instance of the chat GUI, and a boolean to see if the client is connected
	private Socket connection;
	private boolean isConnected = false;
	private Chat gui;
	
		//constructor
		public Client(String host, String port, Chat gui)
		{
			this.host = host;
			this.port = port;
			this.gui = gui;
			
			//start running the client code
			startRunning();
		}
		
		//check the connection of the client
		public boolean checkConnectionStatus()
		{
			return isConnected;
		}
		
		//connect to server
		public void startRunning()
		{
			try
			{
				connectToServer();
				
				if(connection == null)
				{
					System.err.println("connection socket is null!");
					isConnected = false;
					return;
				}
				
				setupStreams();
				
				// send object which has user's information
				RSA clientRSA = gui.getRSA();
				String clientName = gui.getActualName();
				NameAndKeyPair clientNameNKey = new NameAndKeyPair(clientRSA.getPubKey(), clientName);
				
				output.writeObject(clientNameNKey);
				output.flush();
				
				new whileChatting();
			}
			catch(EOFException eofException)
			{
				System.err.println("Client terminated the connection");
			}
			catch(IOException ioException)
			{
				System.err.print("Error with setup");
				isConnected = false;
			}
		}
		
		//connect to server
		private void connectToServer() throws IOException
		{
			try 
			{
				System.out.println("Inside Client: " + port + " " + host);
				connection = new Socket(host, Integer.parseInt(port));
				isConnected = true;
				

				
			}
			catch (NumberFormatException e)
			{

				System.err.println("NumberFormatException Caught!");
			}
		}
		
		//set up streams
		private void setupStreams() throws IOException
		{
			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			
			input = new ObjectInputStream(connection.getInputStream());
		}

		//Close connection
		public void closeConnection()
		{
			try
			{
				output.close();
				input.close();
				connection.close();
			}
			catch(IOException ioException)
			{

				System.err.println("Error with closing");
			}
		}
		
		//send message to server
		public void sendMessage(DataChunk chunkOfData)
		{
			try
			{
				output.writeObject(chunkOfData);
				output.flush();
			}
			catch(IOException ioException)
			{
				System.err.println("Client failed sending the message");
			}
		}
		

//private class for when the client is chatting
private class whileChatting implements Runnable
{
	
	public whileChatting() 
	{
		new Thread(this).start();
	}

	public void run() 
	{
		
		//try and catch's for when the client is sending messages
		do {
			try
			{
				
				//read in the object of the client and send out the message
				Object o = input.readObject();
				
				if(o instanceof NameAndKeyPair)
				{
					NameAndKeyPair sentData = (NameAndKeyPair)o;
					gui.updateClientList(sentData);
				}
				else
				{
					DataChunk message = (DataChunk) o;
					if(message == null)
						System.err.println("Oh no! client recieved null dataChunk!");
					else
					{
						System.out.println("Recieved message from: " + message.getSender());
						gui.appendMessage(message);
					}
				}
			}
			catch(ClassNotFoundException classNotFoundException)
			{
				System.err.println("ClassNotFound Exception triggered...");
			}
			catch(IOException e) 
			{	
				//System.err.println("IO Exception triggered...");
				isConnected = false;
			}
			
		}while(isConnected);
	
		closeConnection();
		
	}
}

}
