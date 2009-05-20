package pipe.user;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import pipe.core.Pipe;
import pipe.main.Main;
import pipe.main.Program;
import base.exception.Error;
import base.user.Cell;
import base.user.Dialog;
import base.user.Panel;

/** The main window on the screen. */
public class Window {
	
	// Links

	private final Program program;
	
	// Window

	/** Make the program's main window on the screen. */
	public Window(Program program) {
		
		// Save the given link
		this.program = program;
		
		// Make panels to show in the window
		toolbar = new Toolbar(program);
		Panel panel = Panel.column();
		panel.add(Cell.wrap(toolbar.panel()));
		for (Pipe pipe : program.pipes)
			panel.add(Cell.wrap(pipe.panel()));

		// Calculate how big the window should be
		Dimension size = new Dimension(Status.width, Toolbar.height + (program.pipes.size() * Status.height));

		
		
		
		
		JFrame.setDefaultLookAndFeelDecorated(true);	
		
		// Make the program's window, configure it, and show it
		JFrame frame = new JFrame();                             // Make the Swing JFrame object which is the program's main window on the screen
		frame.setResizable(false);                               // User can't drag borders to resize
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Make closing the window close the program
		frame.addWindowListener(new MyWindowListener());         // Have Java tell us when the user closes the window
		frame.setTitle(Main.name);                               // Set the text in the window's title bar
		frame.setSize(size);                                     // Set the window's size and location from what we calculated above
		frame.setLocation(Dialog.position(size));                // Pick a random location on the screen
		frame.setContentPane(panel.jpanel);                      // Put the tabs in the window
		frame.setVisible(true);                                  // Show the window on the screen
	}
	
	public final Toolbar toolbar;

	// When the user clicks the main window's corner X, Java calls this windowClosing() method
	private class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent w) {
			try {
				
				// Close the program
				program.close();
				
			} catch (Exception e) { Error.error(e); }
		}
	}
}
