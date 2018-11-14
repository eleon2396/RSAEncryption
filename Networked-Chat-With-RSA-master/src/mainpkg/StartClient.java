/*
 * This is the class that is ran to execute the Client program that will join a server if it exists.
 * it calls the appropriate object to begin the process and continues from there.
 */
package mainpkg;

import Interfacepkg.Chat;

public class StartClient
{
	public static void main(String[] args)
	{
		// begins client program
		Chat.getChatContainer();
	}
}
