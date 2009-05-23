package pipe.user;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import pipe.main.Program;
import base.exception.Mistake;
import base.state.Close;

/** The Info window that shows advanced statistics and diagnostic information. */
public class InfoFrame extends Close {
	
	// Links
	
	private final Program program;
	
	// Object

	public InfoFrame(Program program) {
		
		// Save the given link
		this.program = program;
		

		frame = new JFrame();
		

		/*
		
		// Make the program's window, configure it, and show it
		frame = new JFrame();                             // Make the Swing JFrame object which is the program's main window on the screen
		frame.setResizable(false);                               // User can't drag borders to resize
//		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Make closing the window close the program
		frame.addWindowListener(new MyWindowListener());         // Have Java tell us when the user closes the window
		frame.setTitle(Main.name);                               // Set the text in the window's title bar
		frame.setSize(size);                                     // Set the window's size and location from what we calculated above
		frame.setLocation(Dialog.position(size));                // Pick a random location on the screen
		frame.setContentPane(panel.jpanel);                      // Put the tabs in the window
		
		
		*/
	}
	
	public final JFrame frame;
	
	

	// When the user clicks the main window's corner X, Java calls this windowClosing() method
	private class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent w) {
			try {
				
				// Close the program
				program.close();
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}


	@Override public void close() {
		if (already()) return;
		
		frame.setVisible(false);
		frame.dispose(); // Dispose the frame so the process can close
	}
	
	
	

}
