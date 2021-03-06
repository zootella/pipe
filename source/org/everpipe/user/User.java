package org.everpipe.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.everpipe.main.Main;
import org.everpipe.main.Program;
import org.zootella.desktop.Desktop;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.user.CornerIcon;
import org.zootella.user.Face;
import org.zootella.user.skin.Skin;

public class User extends Close {

	public User(Program program) {
		this.program = program;
		
		restoreAction = new RestoreAction();
		exitAction = new ExitAction();
		
		skin = new Skin(program.store.folder.add("skin.png"), Guide.skinSize);

		main = new MainFrame(this);
		info = new InfoFrame(this);

		if (!Desktop.isMac()) // On Mac, we've already got the icon on the dock
			icon = new CornerIcon(Main.name, Face.image(Guide.icon), restoreAction, exitAction);

		show(true);
	}
	
	public final Program program;
	public final Skin skin;
	public final MainFrame main;
	public final InfoFrame info;
	public CornerIcon icon;
	
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
		if (is(icon))
			icon.show(!show);
	}
	private boolean show;

	public final RestoreAction restoreAction;
	public class RestoreAction extends AbstractAction {
		public RestoreAction() { super("Restore"); }
		public void actionPerformed(ActionEvent a) {
			try {
				show(true);
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}

	public final ExitAction exitAction;
	public class ExitAction extends AbstractAction {
		public ExitAction() { super("Exit"); }
		public void actionPerformed(ActionEvent a) {
			try {
				close(program);
			} catch (Throwable t) { Mistake.stop(t); }
		}
	}
}
