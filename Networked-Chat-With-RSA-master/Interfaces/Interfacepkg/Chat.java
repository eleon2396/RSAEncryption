/* Chat class contains the Frame for all UI elements
 * and implements a Singleton design pattern
 * since we'll only need one instance of the
 * "parent" container
 */

package Interfacepkg;
import Securitypkg.RSA;
import Controllerpkg.*;
import Networkpkg.Client;
import Networkpkg.NameAndKeyPair;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.io.*; 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class Chat extends JFrame implements ActionListener{
	private static Chat CONTAINER = null; // initialize instance to null for singleton design
	
	  // GUI items
	  private JButton sendButton; // send message
	  private JButton connectButton; // join chat
	  private JButton leaveChat; // leave chat
	  private JLabel serverAddressPrompt;
	  private JLabel serverPortPrompt;
	  private JTextField addressInfo;
	  private JTextField portInfo;
	  private JTextField message;
	  private JTextField clientName;
	  private MessageBox mb;	  
	  private Client client = null;
	  private RSA mainRSA;	  
	  
	  // Big Integer type used for primes since their value assumed to be greater than int
	  static BigInteger firstPrime;
	  static BigInteger secondPrime;
  
	  // this keeps track of every client that is connected to the server
	  private Vector<NameAndKeyPair> connectedClients = new Vector<NameAndKeyPair>();
	
	private Chat() 
	{
		JPanel container = new JPanel();
		container.setLayout(new GridLayout(1,2)); // one row, two columns layout
		JPanel left = new JPanel(); 
		JPanel connectionPanel = new JPanel();
		JPanel right = new JPanel();
		left.setLayout(new BorderLayout());
		connectionPanel.setLayout(new GridLayout(2,4));
		JLabel createName = new JLabel(" Your Name");
		clientName = new JTextField();
		connectButton = new JButton("Join Chat");
		connectButton.setEnabled(false); //set enabled until user inputs all data
		leaveChat = new JButton("Leave Chat");
		leaveChat.setEnabled(false); 
		serverAddressPrompt = new JLabel(" Server Address");
		serverPortPrompt = new JLabel(" Server Port");
		addressInfo = new JTextField();
		portInfo = new JTextField();
		
		// add validation for client Name input
		// exit if cancel is clicked or if nothing is typed
		// client name is needed for server to identify connected clients
		try {
			clientName.setText(JOptionPane.showInputDialog(this, "Type in your Name:"));
			if(clientName.getText() == null || (clientName.getText() != null && ("".equals(clientName.getText()))))
			{
				throw new Exception();
			}
		}
		catch(Exception o)
		{
			System.err.println("Name error, field is empty");
			System.exit(0);
		}

		clientName.setEnabled (false);
		connectionPanel.add(createName);
		connectionPanel.add(clientName);
		connectionPanel.add(connectButton);
		connectButton.addActionListener(new JoinChatEventHandler(this));
		connectionPanel.add(leaveChat);	
		leaveChat.setEnabled(false);
		leaveChat.addActionListener(new LeaveChatEventHandler(this));
		connectionPanel.add(serverAddressPrompt);
		connectionPanel.add(addressInfo);
		connectionPanel.add(serverPortPrompt);
		connectionPanel.add(portInfo);
		connectionPanel.setBackground(new Color(204, 255, 245));
		
		left.add(connectionPanel, BorderLayout.NORTH);
		
		mb = new MessageBox(); // message history
		left.add(mb, BorderLayout.CENTER);
		
		JPanel messagePart = new JPanel();
		message = new JTextField(25);
		message.setEnabled (false);
		sendButton = new JButton("Send");
		sendButton.addActionListener( new SendMessageHandler(this) );
		sendButton.setEnabled (false); // keep enabled until connected to server
		
		messagePart.add(message);
		messagePart.add(sendButton);
		messagePart.setBackground(new Color(204, 255, 245));
		left.add(messagePart, BorderLayout.SOUTH);		
		right.add(ClientList.getClientList());		
		right.setBackground(new Color(204, 255, 245));
		container.add(left);
		container.add(right);

		this.add(container);
		setJMenuBar(Menu.getMenu());
		this.setSize(800, 500);
		this.setTitle("Networked Chat with RSA");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		initiateOption(); // get prime numbers generated based on user"s choice
		getConnectionInfo(); // get port and IP address from user input
	}
	
	// vector represents client list
	public Vector<DataChunk> getClientsToSendMsg()
	{
		ClientList boxRef = ClientList.getClientBox();
		List<String> chosenNames =  boxRef.getChosenClients();
		
		if(chosenNames.isEmpty())
		{
			System.err.println("Click on people to send message to!");
			return null;
		}
		
		MessageBox.addMessage("You - " + message.getText());
		
		// init vectors that will be packaged and sent
		Vector<NameAndKeyPair> pairs = new Vector<NameAndKeyPair>();
		Vector<DataChunk> dk = new Vector<DataChunk>();
		Vector<BigInteger> cipherText;
		String sender = clientName.getText();
		
		// n^2 algorithm :c that gets user name and key pair that were selected
		for(String n : chosenNames)
			for(NameAndKeyPair p : connectedClients)
				if(n.equalsIgnoreCase(p.getName()))
					pairs.addElement(p);
		
		// for each selected user encrypt message with their own 
		// public key then package and push to the vector that will be
		// returned
		for(NameAndKeyPair p : pairs)
		{
			cipherText = RSA.encryptM(message.getText(), p.getPubKey());
			
			DataChunk newChunk = new DataChunk(sender, cipherText, p);
			dk.add(newChunk);
		}
		
		return dk;
	}
	
	// when new client is joined, update client list container
	public void updateClientList(NameAndKeyPair newClient)
	{ 
		ClientList ref = ClientList.getClientBox();
		
		if(newClient.getPubKey() == null)
		{
			connectedClients.remove(newClient);
			ref.removeClient(newClient.getName());
			MessageBox.addMessage(newClient.getName() + " has left.");
		}
		else
		{
			connectedClients.addElement(newClient);
			ref.addNewClient(newClient.getName());
			MessageBox.addMessage(newClient.getName() + " has joined.");
		}
	}

	public JTextField getIPaddress()
	{
		return addressInfo;
	}
	
	public JTextField getPort()
	{
		return portInfo;
	}
	
	public JTextField getNameInfo()
	{
		return clientName;
	}

	public String getActualName() {
		return clientName.getText();
	}
	
	// first and second prime numbers
	// are generated from file or from user input
	public static BigInteger getFirstPrime()
	{
		return firstPrime;
	}
	
	public static BigInteger getSecondPrime()
	{
		return secondPrime;
	}
	
	private void setFirstPrime(String num)
	{

		firstPrime = new BigInteger(num);
	}
	
	private void setSecondPrime(String num)
	{
		secondPrime = new BigInteger(num);
	}
	
	// readPrimes is responsible for taking two prime numbers
	// depending on user choice
	// call to RSA checks prime numbers validity
	
	void readPrimes(String fileName)
	{
		 
		String line;
		ArrayList<String> primes = new ArrayList<String>(); // change size later
		int counter = 0;
		
		try {
	           // FileReader reads text files in the default encoding.
	           FileReader fileReader = 
	               new FileReader(fileName);

	           // Always wrap FileReader in BufferedReader.
	           BufferedReader bufferedReader = 
	               new BufferedReader(fileReader);

	           // read one line from file
	           
	           while((line = bufferedReader.readLine()) != null) {
	        	   primes.add(line);
	        	   counter++;
	           } 
	           
    	   Random rand = new Random();
    	   int value = rand.nextInt(counter-1); 	   
	       setFirstPrime(primes.get(value));	           
	       value = rand.nextInt(counter-1);
	       setSecondPrime(primes.get(value));
	       
	       RSA tempRSA = null;
	       boolean checkRSA;
	           try {
	           // set rsa thing in here too
	        	         tempRSA = new RSA(getFirstPrime(), getSecondPrime());
	                 checkRSA = tempRSA.isInputValid();
	           }
	        	   catch(NumberFormatException e) {
	        	        checkRSA = false;
	           }
	           catch(ArithmeticException e) {
	        		   checkRSA = false;
	           }
	           
		   mainRSA = tempRSA;
	           
	           bufferedReader.close();
	           fileReader.close();
	       }
	       catch(FileNotFoundException ex) {
	           System.out.println(
	               "Unable to open file '" + 
	               fileName + "'");                
	       }
	       catch(IOException ex) {
	           System.out.println(
	               "Error reading file '" 
	               + fileName + "'");                  
	           // Or we could just do this: 
	           ex.printStackTrace();
	       }
	}

	public void allowAccess(Client cl)
	{
		client = cl;
		message.setEnabled(true);
		sendButton.setEnabled(true);
		connectButton.setEnabled(false);
		leaveChat.setEnabled(true);
		MessageBox.addMessage("You can send and receive messages now");
	}
	
	public void appendMessage(DataChunk msg)
	{
		String decodedMSG = mainRSA.decryptM(msg.getEncondedMessage());
		MessageBox.addMessage(msg.getSender() + " - " + decodedMSG);
	}
	
	// getter for portInfo based on user input
	public String getPortInfo()
	{
		return portInfo.getText();
	}
	
	// getter ip address based on user input
	public String getIPInfo()
	{
		return addressInfo.getText();
	}	
	
	public RSA getRSA() 
	{
		return mainRSA;
	}
	
	// getter for the message the user input
	public String getMessage()
	{
		return message.getText();
	}
	
	public Client getClientSocket()
	{
		return client;
	}
		
	// singleton design implementation
	public static Chat getChatContainer() 
	{
		if(CONTAINER == null)
		{
			CONTAINER = new Chat();
		}
		
	    return CONTAINER;
	}

	
	// driver function that takes user choice for prime numbers generation 
	void initiateOption() 
	{
		int choice;
		int secondChoice;
		String message = "Would you like to generate Public/Private Key pair Yourself?\n"
			    + "Press Yes to create prime numbers\n"
			    + "No will generate random prime numbers";
		choice = JOptionPane.showConfirmDialog(null, message, "Generate Key Options", JOptionPane.YES_NO_OPTION);
	        if (choice == JOptionPane.YES_OPTION) 
	        {
	        		secondChoice = JOptionPane.showConfirmDialog(null, "Choose your File to load from (YES)\nInput yor own primes(NO)?", "Generate Key Options", JOptionPane.YES_NO_OPTION);
	        		if (secondChoice == JOptionPane.YES_OPTION) 
	        		{
	        			JFileChooser fileChooser = new JFileChooser(".");
	        			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); // specify current working directory
	        			int result = fileChooser.showOpenDialog(this);
					if (result == JFileChooser.APPROVE_OPTION) 
					{
					    File selectedFile = fileChooser.getSelectedFile();
					    readPrimes(selectedFile.getAbsolutePath());
					}
		          return;
		        }
		        else
		        {
			        	RSA tempRSA = null;
			        	boolean checkRSA;
			        	try {
			        		setFirstPrime(JOptionPane.showInputDialog(this, "Type in your First Prime Number:"));
			        		setSecondPrime(JOptionPane.showInputDialog(this, "Type in your Second Prime Number:"));
			        	 	tempRSA = new RSA(getFirstPrime(), getSecondPrime());
				        	checkRSA = tempRSA.isInputValid();
			        	}
			        	catch(NumberFormatException e) {
			        		checkRSA = false;
			        	}
			        	catch(ArithmeticException e) {
			        		checkRSA = false;
			        	}
				    //testing for RSA encryptions
				    if(checkRSA ==  false) {
				    		//repromptUser
				    		while(checkRSA ==  false) {
				    		 	setFirstPrime(JOptionPane.showInputDialog(this, "ERROR please type in your First Prime Number:"));
					        	setSecondPrime(JOptionPane.showInputDialog(this, "ERROR please type in your Second Prime Number:"));
					        	//make rsa thing here
						    tempRSA = new RSA(getFirstPrime(), getSecondPrime());
						    checkRSA = tempRSA.isInputValid();
				    		}
				    }
				    else {
				    		mainRSA = tempRSA;
				    }
		        }
	        }
	        else 
	        {
	        		readPrimes("Resource//primeNumbers.rsc");
	        }
	}
	
	void getConnectionInfo()
	{
		addressInfo.setText(JOptionPane.showInputDialog(this, "Type in IP Address:"));
		portInfo.setText(JOptionPane.showInputDialog(this, "Type in Port:"));
		connectButton.setEnabled(true);
		leaveChat.setEnabled(true);
	}
	
	public void callCloseConnection()
	{
		connectButton.setEnabled(true);
		leaveChat.setEnabled(false);
		client.closeConnection();
	}
	
	// when user reenter the chat, clear clientInfo window
	public void clearUserList()
	{
		ClientList cl = ClientList.getClientBox();
		cl.emptyClientList();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}