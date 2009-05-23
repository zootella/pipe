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
		
		
		
		
		mainWindow = new MainWindow(program);
		infoWindow = new InfoWindow(program);
		mainIcon = new MainIcon(program);
		
		show(true);
		
		
		
		
	}
	
	public final MainWindow mainWindow;
	public final InfoWindow infoWindow;
	public final MainIcon mainIcon;
	
	
	

	private boolean show;
	public void show(boolean b) {
		if (show == b) return;
		
		if (b) {
			mainWindow.frame.setVisible(true);
			mainIcon.show(false);
		} else {
			mainWindow.frame.setVisible(false);
			infoWindow.frame.setVisible(false);
			mainIcon.show(true);
		}
		
		show = b;
	}
	
	

	@Override public void close() {
		if (already()) return;
		
		close(mainWindow);
		close(infoWindow);
		close(mainIcon);
	}
	
	

}
