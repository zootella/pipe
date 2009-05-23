package pipe.user;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import pipe.core.Pipe;
import pipe.main.Main;
import pipe.main.Program;
import base.exception.Mistake;
import base.state.Close;
import base.user.Cell;
import base.user.Dialog;
import base.user.Panel;

/** The main window on the screen that lists the running pipes. */
public class MainFrame extends Close {
	
	// Links

	private final Program program;
	
	// Object

	/** Make the program's main window on the screen. */
	public MainFrame(Program program) {
		
		// Save the given link
		this.program = program;
		
		// Make panels to show in the window
		toolbar = new ToolPanel(program);
		Panel panel = Panel.column();
		panel.add(Cell.wrap(toolbar.panel()));
		for (Pipe pipe : program.core.pipes)
			panel.add(Cell.wrap(pipe.panel()));

		// Calculate how big the window should be
		Dimension size = new Dimension(PipePanel.width, ToolPanel.height + (program.core.pipes.size() * PipePanel.height));

		// Make the program's window, configure it, and show it
		frame = new JFrame();                            // Make the Swing JFrame object which is the program's main window on the screen
		frame.setResizable(false);                       // User can't drag borders to resize
		frame.addWindowListener(new MyWindowListener()); // Have Java tell us when the user closes the window
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("pipe/icon.gif")));
		frame.setTitle(Main.name);                       // Set the text in the window's title bar
		frame.setSize(size);                             // Set the window's size and location from what we calculated above
		frame.setLocation(Dialog.position(size));        // Pick a random location on the screen
		frame.setContentPane(panel.jpanel);              // Put the tabs in the window
	}
	
	public final JFrame frame;
	
	
	public final ToolPanel toolbar;

	// When the user clicks the main window's corner X, Java calls this windowClosing() method
	private class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent w) {
			try {
				
				if (program.core.pipes.size() == 0)
					program.close();
				else
					program.user.show(false);
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}

	@Override public void close() {
		if (already()) return;
		
		frame.setVisible(false);
		frame.dispose(); // Dispose the frame so the process can close
		
	}
}
