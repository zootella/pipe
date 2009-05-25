package pipe.main;

import javax.swing.JDialog;
import javax.swing.JFrame;

import pipe.core.Pipe;
import pipe.user.ExchangeDialog;
import pipe.user.FolderDialog;
import pipe.user.InfoFrame;
import pipe.user.MainIcon;
import pipe.user.MainFrame;
import pipe.user.MuseumDialog;
import base.data.Outline;
import base.file.Path;
import base.state.Close;

public class User extends Close {

	// Object
	
	private final Program program;
	
	public User(Program program) {
		this.program = program;
		
		
		// Java settings
		JFrame.setDefaultLookAndFeelDecorated(true);	
		JDialog.setDefaultLookAndFeelDecorated(true);
		
		
		
		
		window = new MainFrame(program);
		info = new InfoFrame(program);
		icon = new MainIcon(program);
		
		show(true);
		
		
		
		
	}
	
	public final MainFrame window;
	public final InfoFrame info;
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
	
	
	
	// Help
	
	public Pipe newPipe() {

		/*
		Pipe pipe = (new MuseumDialog(program)).result();
		if (pipe == null) return null;
		
		String title, instruction;
		if (type.equals("send")) {
			title = "Send Pipe";
			instruction = "Choose the folder you want to send:";
		} else if (type.equals("receive")) {
			title = "Receive Pipe";
			instruction = "Choose an empty folder to receive the incoming:";
		} else {
			return null;
		}
		
		Path folder = (new FolderDialog(program, title, instruction)).result();
		if (folder == null) return null;

		Outline away = (new ExchangeDialog(program)).result();
		if (away == null) return null;
		*/
		
		
		
		
		
		
		
		return null;
	}
	
	
	

}
