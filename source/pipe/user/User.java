package pipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import pipe.main.Main;
import pipe.main.Program;
import base.process.Mistake;
import base.state.Close;
import base.user.CornerIcon;
import base.user.Face;
import base.user.skin.Skin;

public class User extends Close {

	public User(Program program) {
		this.program = program;
		
		restoreAction = new RestoreAction();
		exitAction = new ExitAction();
		
		skin = new Skin("skin.png");

		main = new MainFrame(this);
		info = new InfoFrame(this);

		icon = new CornerIcon(Main.name, Face.image("pipe/icon.gif"), restoreAction, exitAction);

		show(true);
	}
	
	public final Program program;
	public final Skin skin;
	public final MainFrame main;
	public final InfoFrame info;
	public final CornerIcon icon;
	
	@Override public void close() {
		if (already()) return;
		
		close(main);
		close(info);
		close(icon);
	}

	public void show(boolean b) {
		if (show == b) return;
		show = b;

		main.frame.setVisible(show);
		icon.show(!show);
	}
	private boolean show;

	public final RestoreAction restoreAction;
	private class RestoreAction extends AbstractAction {
		public RestoreAction() { super("Restore"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				show(true);
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}

	public final ExitAction exitAction;
	private class ExitAction extends AbstractAction {
		public ExitAction() { super("Exit"); }
		public void actionPerformed(ActionEvent a) {
			try {
				
				close(program);

			} catch (Exception e) { Mistake.stop(e); }
		}
	}
}
