package Interfacepkg;

/* Menu class represents a Singleton design
 * since it's instance is the same for clients and only needs to be
 * created once.
 * Contains "info" and "about" boxes 
 */
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Menu extends JMenuBar implements ActionListener {
	JMenu menu;
	JMenuItem menuItem;
	private static Menu m;
	//JMenu Help box section is divided in two sub menu items for easier use
	private Menu() {
		  menu = new JMenu("Menu");
		  	menuItem = new JMenuItem("Help Menu");
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(menu, 
							"Help:"
							+ "\nto use Networked Chat with RSA Encryption/Decryption"
							+ "\nClients join to chat and before joining,"
							+ "\n1. Each client is prompted to input their names for identification"
							+ "\nWindow is closed if clients clicks cancel or inputs empty name"
							+ "\nsince name is needed to be nested in clientList"
							+ "\n2. Options to read prime numbers (2) for algorithm are:"
							+ "\ngenerate randomly, choose from file, or input your own"
							+ "\nThose prime numbers are being validated"
							+ "\n3. Input IP address and port, for local run use:"
							+ "\n127.0.0.1 and 1997"
							+ "\n4. After that connect button becomes available"
							+ "\n5. When successfully joining chat "
							+ "\nUser can see list of all connected clients, and"
							+ "\ncan select single or multiple people to send message to."
							+ "\nAll connected clients have updated information about new user"
							+ "\nand can send/receive message between them."
							+ "\nClient can Leave chat and Join back.\n");
					}	  
			});
		  menu.add(menuItem);
			menuItem = new JMenuItem("About"); // battleship game rules
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(menu, 
							"Authors:"
							+ "Mariia Melnikova (mmelni4)"
							+ "\nAlexis Urquiza (aurqui7)"
							+ "\nEric Leon (eleon23)");
				}
			});
		menu.add(menuItem);
		
		this.add(menu);
		
	//...for each JMenuItem instance:
	menuItem.addActionListener(this);
	
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static Menu getMenu()
	{
		if(m == null)	
			m = new Menu();
		return m;
	}
}
