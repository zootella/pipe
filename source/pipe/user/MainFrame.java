package pipe.user;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pipe.core.museum.Pipe;
import pipe.main.Main;
import pipe.main.Mistake;
import pipe.main.Program;
import base.state.Close;
import base.user.Dialog;
import base.user.Screen;

/** The main window on the screen that lists the running pipes. */
public class MainFrame extends Close {

	// Object

	/** Make the program's main window on the screen. */
	public MainFrame(Program program) {
		this.program = program;
		
		frame = new JFrame();
		panel = new JPanel();
		pipes = new JPanel();
		
		frame.setResizable(false);
		frame.setLayout(null);
		panel.setLayout(null);
		pipes.setLayout(null);

		tool = new ToolPanel(program);
		tool.panel.setLocation(0, 0);
		panel.add(tool.panel);

		pipes.setLocation(0, ToolPanel.height);
		fill();
		panel.add(pipes);
		
		frame.addWindowListener(new MyWindowListener()); // Have Java tell us when the user closes the window
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("pipe/icon.gif")));
		frame.setTitle(Main.name);
		frame.setBounds(Screen.positionPixel(frame.getSize().width, frame.getSize().height));
		frame.setContentPane(panel);
	}

	public void fill() {
		
		final int border = 5;
		final int title = 23;
		
		int x = PipePanel.width;
		int y = program.core.pipes.pipes.size() * PipePanel.height;
		pipes.setSize(x, y);
		
		y += ToolPanel.height;
		panel.setSize(x, y);
		
		x += border + border;
		y += border + title + border;
		frame.setSize(x, y);
		
		pipes.removeAll();
		int i = 0;
		for (Pipe pipe : program.core.pipes.pipes) {
			JPanel panel = pipe.panel().panel;
			panel.setLocation(0, i);
			pipes.add(panel);
			i += PipePanel.height;
		}
	}

	private final Program program;

	public final JFrame frame;
	public final JPanel panel;
	private final JPanel pipes;
	
	public final ToolPanel tool;

	// When the user clicks the main window's corner X, Java calls this windowClosing() method
	private class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent w) {
			try {
				
				if (program.core.pipes.pipes.isEmpty())
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
