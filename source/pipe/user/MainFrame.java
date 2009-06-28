package pipe.user;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

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

	// Object
	
	public static final int outsideBorder = 5;
	public static final int outsideTitle = 23;

	/** Make the program's main window on the screen. */
	public MainFrame(Program program) {
		
		// Save the given link
		this.program = program;
		
		// Make panels to show in the window
		tool = new ToolPanel(program);
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(PipePanel.width, 600);
		
		tool.panel.setLocation(0, 0);
		panel.add(tool.panel);
		
		
		/*
		for (Pipe pipe : program.core.pipes)
			column.add(Cell.wrap(pipe.panel().panel.jpanel));
			*/

		
		// Calculate how big the window should be
		Dimension size = new Dimension(PipePanel.width + (2 * outsideBorder), ToolPanel.height + (2 * outsideBorder) + outsideTitle);

		// Make the program's window, configure it, and show it
		frame = new JFrame();                            // Make the Swing JFrame object which is the program's main window on the screen
		frame.setResizable(false);                       // User can't drag borders to resize
		frame.setLayout(null);
		frame.setSize(size);
		
		
		frame.addWindowListener(new MyWindowListener()); // Have Java tell us when the user closes the window
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("pipe/icon.gif")));
		frame.setTitle(Main.name);                       // Set the text in the window's title bar
		frame.setLocation(Dialog.position(size));        // Pick a random location on the screen
		
		
		
		frame.setContentPane(panel);             // Put the tabs in the window
	}

	private final Program program;
	public final JFrame frame;
	public final ToolPanel tool;
	public final JPanel panel;

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
	
	
	public void remove(JPanel pipe) {
		
	}
	

	
	
	
	
	
	
	
	
	
	
}
