package org.everpipe.user;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.everpipe.main.Program;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.state.View;
import org.zootella.user.Refresh;
import org.zootella.user.Screen;
import org.zootella.user.panel.Cell;
import org.zootella.user.panel.Panel;
import org.zootella.user.widget.TextValue;

/** The Info window that shows advanced statistics and diagnostic information. */
public class InfoFrame extends Close {

	public InfoFrame(User user) {
		program = user.program;
		
		refreshCenterAction = new RefreshCenterAction();
		refreshLanAction = new RefreshLanAction();
		
		lan = new TextValue();
		internet = new TextValue();
		age = new TextValue();
		exception = new TextValue();

		panel = new Panel();
		panel.border();
		
		//          0, 0
		panel.place(0, 1, 1, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("LAN")));
		panel.place(0, 2, 1, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("Bind")));
		panel.place(0, 3, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("NAT")));
		//          0, 4
		//          0, 5
		//          0, 6
		panel.place(0, 7, 1, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("Center")));
		
		panel.place(1, 0, 4, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("1.2.3.4:5 -> 6.7.8.9:5")));
		panel.place(1, 1, 1, 1, 0, 0, 1, 1, Cell.wrap(lan.area));
		panel.place(1, 2, 1, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("1001")));
		panel.place(1, 3, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("Linksys Model")));
		panel.place(1, 4, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("1.2.3.4")));
		panel.place(1, 5, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("5 -> 1.2.3.4:5 TCP Pipe")));
		panel.place(1, 6, 1, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("5 -> 1.2.3.4:5 UDP Pipe")));
		panel.place(1, 7, 1, 1, 0, 0, 1, 1, Cell.wrap(internet.area));

		//          2, 0
		panel.place(2, 1, 1, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("Thu")));
		panel.place(2, 2, 1, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("Thu")));
		panel.place(2, 3, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("Thu")));
		panel.place(2, 4, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("Thu")));
		panel.place(2, 5, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("Thu")));
		panel.place(2, 6, 1, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("Thu")));
		panel.place(2, 7, 1, 1, 0, 0, 1, 1, Cell.wrap(age.area));
		
		//          3, 0
		panel.place(3, 1, 1, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("err")));
		panel.place(3, 2, 1, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("err")));
		panel.place(3, 3, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("err")));
		panel.place(3, 4, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("err")));
		panel.place(3, 5, 1, 1, 0, 0, 0, 1, Cell.wrap(new JLabel("err")));
		panel.place(3, 6, 1, 1, 0, 0, 1, 1, Cell.wrap(new JLabel("err")));
		panel.place(3, 7, 1, 1, 0, 0, 1, 1, Cell.wrap(exception.area));

		//          4, 0
		panel.place(4, 1, 1, 1, 0, 0, 1, 1, Cell.wrap(new JButton(refreshCenterAction)));
		panel.place(4, 2, 1, 1, 0, 0, 1, 1, Cell.wrap(new JButton(refreshCenterAction)));
		panel.place(4, 3, 1, 4, 0, 0, 0, 1, Cell.wrap(new JButton(refreshCenterAction)));
		//          4, 4
		//          4, 5
		//          4, 6
		panel.place(4, 7, 1, 1, 0, 0, 1, 1, Cell.wrap(new JButton(refreshCenterAction)).grow());

		// Make our inner View object and connect the Model below to it
		view = new MyView();
		program.core.hereOld.model.add(view); // When the Model below changes, it will call our view.refresh() method
		view.refresh();

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(Guide.icon)));
		frame.setTitle("Information");
		frame.setBounds(Screen.positionSize(Guide.sizeInfoFrame));
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
	
	
	
	

	private final RefreshLanAction refreshLanAction;
	private class RefreshLanAction extends AbstractAction {
		public RefreshLanAction() { super("Refresh"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {
			try {
				
				
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}
	
	private final RefreshCenterAction refreshCenterAction;
	private class RefreshCenterAction extends AbstractAction {
		public RefreshCenterAction() { super("Refresh"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {
			try {
				program.core.hereOld.refresh();
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}
	
	
	// View

	// When our Model underneath changes, it calls these methods
	private final View view;
	private class MyView implements View {

		// The Model beneath changed, we need to update what we show the user
		public void refresh() {
			Refresh.text(lan.area, program.core.hereOld.model.lan());
			Refresh.text(internet.area, program.core.hereOld.model.internet());
			Refresh.text(age.area, program.core.hereOld.model.age());
			Refresh.text(exception.area, program.core.hereOld.model.exception());
		}

		// The Model beneath closed, take this View off the screen
		public void vanish() { close(InfoFrame.this); }
	}
	
	
	
	
	
	
	
	
	
}
