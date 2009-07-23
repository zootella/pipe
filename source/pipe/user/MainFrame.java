package pipe.user;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pipe.core.museum.Pipe;
import pipe.main.Main;
import pipe.main.Program;
import pipe.user.skin.Guide;
import base.process.Mistake;
import base.state.Close;
import base.user.Screen;

/** The main window on the screen that lists the running pipes. */
public class MainFrame extends Close {

	// Object

	/** Make the program's main window on the screen. */
	public MainFrame(User user) {
		program = user.program;
		
		frame = new JFrame();
		panel = new JPanel();
		pipes = new JPanel();
		
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setLayout(null);
		panel.setLayout(null);
		pipes.setLayout(null);

		tool = new ToolPanel(user, this);
		tool.panel.setLocation(0, 0);
		panel.add(tool.panel);

		pipes.setLocation(0, Guide.toolHeight);
		fill();
		panel.add(pipes);
		
		frame.addWindowListener(new MyWindowListener()); // Have Java tell us when the user closes the window
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("pipe/icon.gif")));
		frame.setTitle(Main.name);
		frame.setBounds(Screen.positionSize(frame.getSize().width, frame.getSize().height));
		frame.setContentPane(panel);
	}

	public void fill() {

		int w = Guide.pipeWidth;
		int h = program.core.pipes.pipes.size() * Guide.pipeHeight;
		pipes.setSize(w, h);
		
		h += Guide.toolHeight;
		panel.setSize(w, h);
		frame.setSize(w, h);
		
		pipes.removeAll();
		int y = 0;
		for (Pipe pipe : program.core.pipes.pipes) {
			JPanel panel = pipe.panel().panel;
			panel.setLocation(0, y);
			pipes.add(panel);
			y += Guide.pipeHeight;
		}
	}

	public final Program program;

	public final JFrame frame;
	public final JPanel panel;
	private final JPanel pipes;
	
	public final ToolPanel tool;

	// When the user clicks the main window's corner X, Java calls this windowClosing() method
	private class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent w) {
			try {
				
				program.user.show(false);
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	@Override public void close() {
		if (already()) return;
		
		frame.setVisible(false);
		frame.dispose(); // Dispose the frame so the process can close
	}
}
