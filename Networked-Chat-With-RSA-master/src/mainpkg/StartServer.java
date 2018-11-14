/*
 * This class is what begins the server program that the client program will need when they attempt to connect to 
 * a server. When the server starts there is ip address and port info that the client needs to connect displayed at the
 * the top
 */
package mainpkg;

import Networkpkg.Server;

public class StartServer
{
	public static void main(String[] args)
	{
		// begin server program
		@SuppressWarnings("unused")
		Server serverTest = new Server();
	}
}
