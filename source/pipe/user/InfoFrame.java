package pipe.user;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import pipe.core.Here;
import pipe.main.Mistake;
import pipe.main.Program;
import base.state.Close;
import base.state.View;
import base.user.Cell;
import base.user.Panel;
import base.user.Refresh;
import base.user.Screen;
import base.user.SelectTextArea;

/** The Info window that shows advanced statistics and diagnostic information. */
public class InfoFrame extends Close {

	public InfoFrame(Program program) {
		this.program = program;
		
		refreshAction = new RefreshAction();
		
		port = new SelectTextArea();
		lan = new SelectTextArea();
		internet = new SelectTextArea();
		age = new SelectTextArea();

		panel = new Panel();
		panel.border();
		
		panel.place(0, 0, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("Chosen port")));
		panel.place(0, 1, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("LAN IP address")));
		panel.place(0, 2, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Internet IP address")));
		panel.place(0, 3, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Age of information")));
		
		panel.place(1, 0, 1, 1, 0, 1, 0, 0, Cell.wrap(port).fillWide());
		panel.place(1, 1, 1, 1, 1, 1, 0, 0, Cell.wrap(lan).fillWide());
		panel.place(1, 2, 1, 1, 1, 1, 0, 0, Cell.wrap(internet).fillWide());
		panel.place(1, 3, 1, 1, 1, 1, 0, 0, Cell.wrap(age).fillWide());
		
		panel.place(1, 4, 1, 1, 1, 1, 0, 0, Cell.wrap(new JButton(refreshAction)).grow());
		
		
		
		
		
		//TODO
		// Chosen port         1234
		// Local IP address    1.2.3.4
		// Internet IP address 1.2.3.4
		// Age of information  22 seconds [ Refresh ]
		
		// make these dialogs have a white background with the little font in light gray


		// Make our inner View object and connect the Model below to it
		view = new MyView();
		program.core.here.model.add(view); // When the Model below changes, it will call our view.refresh() method
		view.refresh();
		
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("pipe/icon.gif")));
		frame.setTitle("Information");
		frame.setBounds(Screen.positionSize(600, 200));
		frame.setContentPane(panel.panel);
	}
	
	private final Program program;
	public final JFrame frame;
	public final Panel panel;
	
	private final SelectTextArea port;
	private final SelectTextArea lan;
	private final SelectTextArea internet;
	private final SelectTextArea age;
	
	

	@Override public void close() {
		if (already()) return;

		frame.setVisible(false);
		frame.dispose(); // Dispose the frame so the process can close
	}
	
	
	
	

	private final RefreshAction refreshAction;
	private class RefreshAction extends AbstractAction {
		public RefreshAction() { super("Refresh"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {
			try {
				
				
			} catch (Exception e) { Mistake.grab(e); }
		}
	}
	
	
	
	// View

	// When our Model underneath changes, it calls these methods
	private final View view;
	private class MyView implements View {

		// The Model beneath changed, we need to update what we show the user
		public void refresh() {
			Refresh.text(port,     program.core.here.model.port());
			Refresh.text(lan,      program.core.here.model.lan());
			Refresh.text(internet, program.core.here.model.internet());
			Refresh.text(age,      program.core.here.model.age());
		}

		// The Model beneath closed, take this View off the screen
		public void vanish() { me().close(); }
	}
	
	/** Give inner classes a link to this outer object. */
	private InfoFrame me() { return this; }
	
	
	
	
	
	
	
	
	
}
