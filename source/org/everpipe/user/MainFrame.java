package org.everpipe.user;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.everpipe.core.museum.Pipe;
import org.everpipe.main.Main;
import org.everpipe.main.Program;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.user.Screen;

/** The main window on the screen that lists the running pipes. */
public class MainFrame extends Close {

	// Object

	/** Make the program's main window on the screen. */
	public MainFrame(User user) {
		program = user.program;

		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setLayout(null);
		
		panel = new JPanel();
		panel.setLayout(null);

		tool = new ToolPanel(user, this);
		tool.panel.setLocation(0, 0);

		fill();
		
		frame.addWindowListener(new MyWindowListener()); // Have Java tell us when the user closes the window
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("pipe/icon.gif")));
		frame.setTitle(Main.name);
		frame.setBounds(Screen.positionSize(frame.getSize().width, frame.getSize().height));
		frame.setContentPane(panel);
	}

	public void fill() {

		Dimension d = new Dimension(Guide.pipeWidth, Guide.toolHeight + (program.core.pipes.pipes.size() * Guide.pipeHeight));
		
		panel.setSize(d);
		frame.setSize(d);

		panel.removeAll();
		panel.add(tool.panel);
		int y = Guide.toolHeight;
		for (Pipe pipe : program.core.pipes.pipes) {
			JPanel p = pipe.userPanel().panel;
			p.setLocation(0, y);
			panel.add(p);
			y += Guide.pipeHeight;
		}
	}

	public final Program program;

	public final JFrame frame;
	public final JPanel panel;
	
	public final ToolPanel tool;

	// When the user clicks the main window's corner X, Java calls this windowClosing() method
	private class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent w) {
			try {
				
				program.user.show(false);
				
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	@Override public void close() {
		if (already()) return;
		
		frame.setVisible(false);
		frame.dispose(); // Dispose the frame so the process can close
	}
}
