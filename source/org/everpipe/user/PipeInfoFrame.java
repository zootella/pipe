package org.everpipe.user;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.zootella.state.Close;
import org.zootella.user.Screen;
import org.zootella.user.panel.Cell;
import org.zootella.user.panel.Panel;
import org.everpipe.core.museum.Pipe;
import org.everpipe.main.Program;

public class PipeInfoFrame extends Close {
	
	public PipeInfoFrame(Program program, Pipe pipe) {
		this.program = program;
		this.pipe = pipe;

		panel = new Panel();
		panel.add(Cell.wrap(new JLabel("The Internet is a series of Pipes")));

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(Guide.icon)));
		frame.setTitle("Pipe Information");
		frame.setSize(200, 200);
		frame.setBounds(Screen.positionPercent(40, 40));
		frame.setContentPane(panel.panel);
	}
	
	private final Program program;
	private final Pipe pipe;
	public final JFrame frame;
	public final Panel panel;
	
	@Override public void close() {
		if (already()) return;

		frame.setVisible(false);
		frame.dispose(); // Dispose the frame so the process can close
	}
}
