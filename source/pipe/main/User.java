package pipe.main;

import javax.swing.JDialog;
import javax.swing.JFrame;

import pipe.core.Pipe;
import pipe.user.InfoFrame;
import pipe.user.MainFrame;
import pipe.user.MainIcon;
import base.state.Close;

public class User extends Close {

	// Object
	
	private final Program program;
	
	public User(Program program) {
		this.program = program;
		
		
		// Java settings
		JFrame.setDefaultLookAndFeelDecorated(true);	
		JDialog.setDefaultLookAndFeelDecorated(true);
		
		
		
		
		frame = new MainFrame(program);
		info = new InfoFrame(program);
		icon = new MainIcon(program);
		
		show(true);
		
		
		
		
	}
	
	public final MainFrame frame;
	public final InfoFrame info;
	public final MainIcon icon;
	
	
	
	public void add(Pipe pipe) {
		frame.add(pipe.panel());
		
		
		
	}
	
	

	private boolean show;
	public void show(boolean b) {
		if (show == b) return;
		
		if (b) {
			frame.frame.setVisible(true);
			icon.show(false);
		} else {
			frame.frame.setVisible(false);
			info.frame.setVisible(false);
			icon.show(true);
		}
		
		show = b;
	}
	
	

	@Override public void close() {
		if (already()) return;
		
		close(frame);
		close(info);
		close(icon);
	}
	
	
	
	
	
	
	

}
