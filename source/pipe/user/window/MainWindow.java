package pipe.user.window;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import pipe.core.Pipe;
import pipe.main.Main;
import pipe.main.Core;
import pipe.main.Program;
import pipe.user.panel.PipePanel;
import pipe.user.panel.ToolPanel;
import base.exception.Error;
import base.state.Close;
import base.user.Cell;
import base.user.Dialog;
import base.user.Panel;

/** The main window on the screen that lists the running pipes. */
public class MainWindow extends Close {
	
	// Links

	private final Program program;
	
	// Object

	/** Make the program's main window on the screen. */
	public MainWindow(Program program) {
		
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
		frame = new JFrame();                                    // Make the Swing JFrame object which is the program's main window on the screen
		frame.setResizable(false);                               // User can't drag borders to resize
		frame.addWindowListener(new MyWindowListener());         // Have Java tell us when the user closes the window
		frame.setTitle(Main.name);                               // Set the text in the window's title bar
		frame.setSize(size);                                     // Set the window's size and location from what we calculated above
		frame.setLocation(Dialog.position(size));                // Pick a random location on the screen
		frame.setContentPane(panel.jpanel);                      // Put the tabs in the window
		
		String name = "pipe/icon.gif"; // The complete package name of the resource
        URL url = ClassLoader.getSystemResource(name); // Have Java find it
        Icon icon = new ImageIcon(url);
        
        Image image = Toolkit.getDefaultToolkit().getImage(url);		
		frame.setIconImage(image);
	}
	
	public final JFrame frame;
	
	
	public final ToolPanel toolbar;

	// When the user clicks the main window's corner X, Java calls this windowClosing() method
	private class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent w) {
			try {
				
				program.user.show(false);
				
			} catch (Exception e) { Error.error(e); }
		}
	}

	@Override public void close() {
		if (already()) return;
		
		frame.setVisible(false);
		frame.dispose(); // Dispose the frame so the process can close
		
	}
}
