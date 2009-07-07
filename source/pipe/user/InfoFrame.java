package pipe.user;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

import pipe.main.Program;
import base.state.Close;
import base.user.Cell;
import base.user.Panel;
import base.user.Screen;

/** The Info window that shows advanced statistics and diagnostic information. */
public class InfoFrame extends Close {

	public InfoFrame(Program program) {
		this.program = program;

		panel = new Panel();
		panel.add(Cell.wrap(new JLabel("In the future, all wars will be fought over Information")));
		
		//TODO
		// Chosen port         1234
		// Local IP address    1.2.3.4
		// Internet IP address 1.2.3.4
		// Age of information  22 seconds [ Refresh ]

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("pipe/icon.gif")));
		frame.setTitle("Information");
		frame.setSize(200, 200);
		frame.setBounds(Screen.positionPercent(60, 60));
		frame.setContentPane(panel.panel);
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
