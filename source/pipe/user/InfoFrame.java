package pipe.user;

import javax.swing.JFrame;

import pipe.main.Program;
import base.state.Close;
import base.user.Panel;

/** The Info window that shows advanced statistics and diagnostic information. */
public class InfoFrame extends Close {

	public InfoFrame(Program program) {
		this.program = program;
		

		frame = new JFrame();
		panel = new Panel();
		
/*
		frame.setTitle("Pipe Info");
		frame.setSize(size);                                     // Set the window's size and location from what we calculated above
		frame.setLocation(Dialog.position(size));                // Pick a random location on the screen
		frame.setContentPane(panel.jpanel);                      // Put the tabs in the window
		
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();          // Screen resolution in pixels
		Dimension size = new Dimension(screen.width * 3 / 4, screen.height / 2); // Window size
		Point location = Dialog.position(size);                                  // Random location
		
		

		frame = new JFrame();                                    // Make the Swing JFrame object which is the program's main window on the screen
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Make closing the window close the program
		frame.setTitle("Information");                           // Set the text in the window's title bar
		frame.setSize(size);                                     // Set the window's size and location from what we calculated above
		frame.setLocation(location);
		frame.setContentPane(tabs);                              // Put the tabs in the window
		frame.setVisible(true);                                  // Show the window on the screen
		
		frame.setBounds(r)
		*/

		
	}
	
	private final Program program;
	public final JFrame frame;
	public final Panel panel;
	
	
	@Override public void close() {
		if (already()) return;
		
		frame.setVisible(false);
		frame.dispose(); // Dispose the frame so the process can close
	}
	
	
	

}
