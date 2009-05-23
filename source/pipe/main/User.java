package pipe.main;

import javax.swing.JFrame;

import pipe.user.window.InfoWindow;
import pipe.user.window.MainIcon;
import pipe.user.window.MainWindow;
import base.state.Close;

public class User extends Close {
	
	private final Program program;
	
	public User(Program program) {
		this.program = program;
		
		
		// Java settings
		JFrame.setDefaultLookAndFeelDecorated(true);	
		
		
		
		
		window = new MainWindow(program);
		info = new InfoWindow(program);
		icon = new MainIcon(program);
		
		show(true);
		
		
		
		
	}
	
	public final MainWindow window;
	public final InfoWindow info;
	public final MainIcon icon;
	
	
	

	private boolean show;
	public void show(boolean b) {
		if (show == b) return;
		
		if (b) {
			window.frame.setVisible(true);
			icon.show(false);
		} else {
			window.frame.setVisible(false);
			info.frame.setVisible(false);
			icon.show(true);
		}
		
		show = b;
	}
	
	

	@Override public void close() {
		if (already()) return;
		
		close(window);
		close(info);
		close(icon);
	}
	
	

}
