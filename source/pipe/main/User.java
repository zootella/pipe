package pipe.main;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pipe.core.Pipe;
import pipe.user.InfoFrame;
import pipe.user.MainFrame;
import pipe.user.MainIcon;
import pipe.user.PipePanel;
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
		
		JPanel panel = pipe.panel().panel;
		panel.setLayout(null);
		panel.setBackground(new Color(0, 0, 0));
		panel.setBounds(0, 150, PipePanel.width, PipePanel.height);
		
		
		frame.panel.add(panel);
		
		
		
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
