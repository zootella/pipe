package bridge.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import base.exception.CodeException;
import base.exception.MessageException;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.state.View;
import base.user.Cell;
import base.user.Panel;
import base.user.Refresh;
import base.user.TextMenu;
import bridge.Bridge;

public class BridgeTab {
	
	// Define
	
	private final Port port;
	private final IpPort ipPort;
	
	// Links
	
	private Bridge bridge;

	// Tab

	/** Make the Bridge tab in the window. */
	public BridgeTab(Bridge bridge) {
		
		this.bridge = bridge;
		
		try {
			port = new Port(1234);
			ipPort = new IpPort("127.0.0.1:1234");
		} catch (MessageException e) { throw new CodeException(); }

		// Make the output text area
		output = new JTextArea();
		output.setEditable(false); // Make it read-only
		new TextMenu(output); // Give it a right-click menu of clipboard commands
		JScrollPane scroll = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Make the command text box
		command = new JTextField();
		new TextMenu(command); // Give it a right-click menu of clipboard commands

		
		Panel bar = Panel.row();
		bar.add(Cell.wrap(new JButton(new ServerAction())));
		bar.add(Cell.wrap(new JButton(new ConnectAction())));

		// Lay out the text boxes and button on a Panel, which will appear in the tab
		panel = new Panel();
		panel.border();
		panel.place(0, 0, 2, 1, 0, 0, 0, 0, Cell.wrap(bar.jpanel));
		panel.place(0, 1, 2, 1, 1, 0, 1, 0, Cell.wrap(scroll).fill());
		panel.place(0, 2, 1, 1, 0, 0, 0, 0, Cell.wrap(command).fillWide());
		panel.place(1, 2, 1, 1, 0, 1, 0, 0, Cell.wrap(new JButton(new SendAction())));
	}
	
	/** A Panel object which contains the Swing JPanel that holds all the user interface components. */
	public final Panel panel;
	
	/** The large text box in the center that prints the program's output. */
	private JTextArea output;
	/** The single-line text box at the bottom where the user can type a command. */
	private JTextField command;
	
	
	
	// Actions

	// The user clicked the Listen button
	private class ServerAction extends AbstractAction {
		public ServerAction() { super("Listen"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {

			bridge.server(port);
			report("listening...");
		}
	}
	
	// The user clicked the Connect button
	private class ConnectAction extends AbstractAction {
		public ConnectAction() { super("Connect"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {
			
			bridge.client(ipPort);
			report("connecting...");
		}
	}

	// The user clicked the Send button
	private class SendAction extends AbstractAction {
		public SendAction() { super("Send"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {
			
			String s = command.getText();
			bridge.send(s);
			report("sending: " + s);
		}
	}

	// Report
    
    /** Print a line of text onto the Status tab. */
    public void report(String s) {
        output.append(s + "\n"); // Add the given text and a newline to the end of what's already in the output box
        output.setCaretPosition(output.getDocument().getLength()); // Scroll to the bottom
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
