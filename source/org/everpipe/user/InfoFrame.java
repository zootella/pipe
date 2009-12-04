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
import org.zootella.user.widget.TextLine;

/** The Info window that shows advanced statistics and diagnostic information. */
public class InfoFrame extends Close {
	
	private final Program program;

	public InfoFrame(User user) {
		program = user.program;
		
		

		panel = new Panel();
		panel.border();
		
		//          0, 0
		panel.place(0, 1, 1, 1, 0, 0, 1, 2, Cell.wrap(new JLabel("LAN")));
		panel.place(0, 2, 1, 1, 0, 0, 1, 2, Cell.wrap(new JLabel("Bind")));
		panel.place(0, 3, 1, 1, 0, 0, 0, 2, Cell.wrap(new JLabel("NAT")));
		//          0, 4
		//          0, 5
		//          0, 6
		panel.place(0, 7, 1, 1, 0, 0, 1, 2, Cell.wrap(new JLabel("Center")));
		
		panel.place(1, 0, 4, 1, 0, 0, 1, 2, Cell.wrap(summary.area));
		panel.place(1, 1, 1, 1, 0, 0, 1, 2, Cell.wrap(lanValue.area));
		panel.place(1, 2, 1, 1, 0, 0, 1, 2, Cell.wrap(bindValue.area));
		panel.place(1, 3, 1, 1, 0, 0, 0, 2, Cell.wrap(natModelValue.area));
		panel.place(1, 4, 1, 1, 0, 0, 0, 2, Cell.wrap(natIpValue.area));
		panel.place(1, 5, 1, 1, 0, 0, 0, 2, Cell.wrap(natTcpValue.area));
		panel.place(1, 6, 1, 1, 0, 0, 1, 2, Cell.wrap(natUdpValue.area));
		panel.place(1, 7, 1, 1, 0, 0, 1, 2, Cell.wrap(centerValue.area));

		//          2, 0
		panel.place(2, 1, 1, 1, 0, 0, 1, 2, Cell.wrap(lanTime.area));
		panel.place(2, 2, 1, 1, 0, 0, 1, 2, Cell.wrap(bindTime.area));
		panel.place(2, 3, 1, 1, 0, 0, 0, 2, Cell.wrap(natModelTime.area));
		panel.place(2, 4, 1, 1, 0, 0, 0, 2, Cell.wrap(natIpTime.area));
		panel.place(2, 5, 1, 1, 0, 0, 0, 2, Cell.wrap(natTcpTime.area));
		panel.place(2, 6, 1, 1, 0, 0, 1, 2, Cell.wrap(natUdpTime.area));
		panel.place(2, 7, 1, 1, 0, 0, 1, 2, Cell.wrap(centerTime.area));
		
		//          3, 0
		panel.place(3, 1, 1, 1, 0, 0, 1, 2, Cell.wrap(lanError.area));
		panel.place(3, 2, 1, 1, 0, 0, 1, 2, Cell.wrap(bindError.area));
		panel.place(3, 3, 1, 1, 0, 0, 0, 2, Cell.wrap(natModelError.area));
		panel.place(3, 4, 1, 1, 0, 0, 0, 2, Cell.wrap(natIpError.area));
		panel.place(3, 5, 1, 1, 0, 0, 0, 2, Cell.wrap(natTcpError.area));
		panel.place(3, 6, 1, 1, 0, 0, 1, 2, Cell.wrap(natUdpError.area));
		panel.place(3, 7, 1, 1, 0, 0, 1, 2, Cell.wrap(centerError.area));

		//          4, 0
		panel.place(4, 1, 1, 1, 0, 0, 1, 2, Cell.wrap(new JButton(lanAction)));
		panel.place(4, 2, 1, 1, 0, 0, 1, 2, Cell.wrap(new JButton(bindAction)));
		panel.place(4, 3, 1, 4, 0, 0, 0, 2, Cell.wrap(new JButton(natAction)));
		//          4, 4
		//          4, 5
		//          4, 6
		panel.place(4, 7, 1, 1, 0, 0, 1, 2, Cell.wrap(new JButton(centerAction)).grow());

		// Make our inner View object and connect the Model below to it
		program.core.here.model.add(view); // When the Model below changes, it will call our view.refresh() method
		view.refresh();//TODO why not put view.refresh() inside add(view)

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(Guide.icon)));
		frame.setTitle("Information");
		frame.setBounds(Screen.positionSize(Guide.sizeInfoFrame));
		frame.setContentPane(panel.panel);
	}
	
	public final JFrame frame;
	public final Panel panel;
	
	private final TextLine summary = new TextLine();
	
	private final TextLine lanValue = new TextLine();
	private final TextLine bindValue = new TextLine();
	private final TextLine natModelValue = new TextLine();
	private final TextLine natIpValue = new TextLine();
	private final TextLine natTcpValue = new TextLine();
	private final TextLine natUdpValue = new TextLine();
	private final TextLine centerValue = new TextLine();
	
	private final TextLine lanTime = new TextLine();
	private final TextLine bindTime = new TextLine();
	private final TextLine natModelTime = new TextLine();
	private final TextLine natIpTime = new TextLine();
	private final TextLine natTcpTime = new TextLine();
	private final TextLine natUdpTime = new TextLine();
	private final TextLine centerTime = new TextLine();
	
	private final TextLine lanError = new TextLine();
	private final TextLine bindError = new TextLine();
	private final TextLine natModelError = new TextLine();
	private final TextLine natIpError = new TextLine();
	private final TextLine natTcpError = new TextLine();
	private final TextLine natUdpError = new TextLine();
	private final TextLine centerError = new TextLine();

	@Override public void close() {
		if (already()) return;

		frame.setVisible(false);
		frame.dispose(); // Dispose the frame so the process can close
	}

	private final LanAction lanAction = new LanAction();
	private class LanAction extends AbstractAction {
		public LanAction() { super("Refresh"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {
			try {
				program.core.here.refreshLan();
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	private final BindAction bindAction = new BindAction();
	private class BindAction extends AbstractAction {
		public BindAction() { super("Refresh"); }
		public void actionPerformed(ActionEvent a) {
			try {
				program.core.here.refreshBind();
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	private final NatAction natAction = new NatAction();
	private class NatAction extends AbstractAction {
		public NatAction() { super("Refresh"); }
		public void actionPerformed(ActionEvent a) {
			try {
				program.core.here.refreshNat();
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	private final CenterAction centerAction = new CenterAction();
	private class CenterAction extends AbstractAction {
		public CenterAction() { super("Refresh"); }
		public void actionPerformed(ActionEvent a) {
			try {
				program.core.here.refreshCenter();
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}
	
	
	// View

	// When our Model underneath changes, it calls these methods
	private final View view = new MyView();
	private class MyView implements View {

		// The Model beneath changed, we need to update what we show the user
		public void refresh() {
			
			Refresh.text(lanValue.area, program.core.here.model.lanIp());
			Refresh.text(bindValue.area, program.core.here.model.bindPort());
			Refresh.text(natModelValue.area, program.core.here.model.natModel());
			Refresh.text(natIpValue.area, program.core.here.model.natIp());
			Refresh.text(natTcpValue.area, program.core.here.model.mapTcp());
			Refresh.text(natUdpValue.area, program.core.here.model.mapUdp());
			Refresh.text(centerValue.area, program.core.here.model.centerIp());
			
			Refresh.text(lanTime.area, program.core.here.model.lanIpTime());
			Refresh.text(bindTime.area, program.core.here.model.bindPortTime());
			Refresh.text(natModelTime.area, program.core.here.model.natModelTime());
			Refresh.text(natIpTime.area, program.core.here.model.natIpTime());
			Refresh.text(natTcpTime.area, program.core.here.model.mapTcpTime());
			Refresh.text(natUdpTime.area, program.core.here.model.mapUdpTime());
			Refresh.text(centerTime.area, program.core.here.model.centerIpTime());
			
			Refresh.text(lanError.area, program.core.here.model.lanIpError());
			Refresh.text(bindError.area, program.core.here.model.bindPortError());
			Refresh.text(natModelError.area, program.core.here.model.natModelError());
			Refresh.text(natIpError.area, program.core.here.model.natIpError());
			Refresh.text(natTcpError.area, program.core.here.model.mapTcpError());
			Refresh.text(natUdpError.area, program.core.here.model.mapUdpError());
			Refresh.text(centerError.area, program.core.here.model.centerIpError());
		}

		// The Model beneath closed, take this View off the screen
		public void vanish() { close(InfoFrame.this); }
	}
	
	
	
	
	
	
	
	
	
}
