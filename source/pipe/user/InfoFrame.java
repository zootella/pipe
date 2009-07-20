package pipe.user;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import pipe.main.Program;
import base.process.Mistake;
import base.state.Close;
import base.state.View;
import base.user.Refresh;
import base.user.Screen;
import base.user.panel.Cell;
import base.user.panel.Panel;
import base.user.widget.TextValue;

/** The Info window that shows advanced statistics and diagnostic information. */
public class InfoFrame extends Close {

	public InfoFrame(Program program) {
		this.program = program;
		
		refreshAction = new RefreshAction();
		
		lan = new TextValue();
		internet = new TextValue();
		age = new TextValue();
		exception = new TextValue();

		panel = new Panel();
		panel.border();
		
		panel.place(0, 0, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("LAN IP address")));
		panel.place(0, 1, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Internet IP address")));
		panel.place(0, 2, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Age of information")));
		
		panel.place(1, 0, 1, 1, 0, 1, 0, 0, Cell.wrap(lan.area).fillWide());
		panel.place(1, 1, 1, 1, 1, 1, 0, 0, Cell.wrap(internet.area).fillWide());
		panel.place(1, 2, 1, 1, 1, 1, 0, 0, Cell.wrap(age.area).fillWide());
		panel.place(1, 3, 1, 1, 1, 1, 0, 0, Cell.wrap(exception.area).fillWide());
		
		panel.place(1, 4, 1, 1, 1, 1, 0, 0, Cell.wrap(new JButton(refreshAction)).grow());
		
		

		// make these dialogs have a white background with the little font in light gray

		// Make our inner View object and connect the Model below to it
		view = new MyView();
		program.core.model.add(view); // When the Model below changes, it will call our view.refresh() method
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
	
	private final TextValue lan;
	private final TextValue internet;
	private final TextValue age;
	private final TextValue exception;
	
	

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
				
				program.core.here.refresh();

			} catch (Exception e) { Mistake.stop(e); }
		}
	}
	
	
	
	// View

	// When our Model underneath changes, it calls these methods
	private final View view;
	private class MyView implements View {

		// The Model beneath changed, we need to update what we show the user
		public void refresh() {
			Refresh.can(refreshAction, program.core.model.canRefresh());
			Refresh.text(lan.area, program.core.model.lan());
			Refresh.text(internet.area, program.core.model.internet());
			Refresh.text(age.area, program.core.model.age());
			Refresh.text(exception.area, program.core.model.exception());
		}

		// The Model beneath closed, take this View off the screen
		public void vanish() { close(me()); }
	}
	
	/** Give inner classes a link to this outer object. */
	private InfoFrame me() { return this; }
	
	
	
	
	
	
	
	
	
}
