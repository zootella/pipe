package pipe.user;

import pipe.main.Program;
import base.state.Close;
import base.user.skin.Skin;

public class User extends Close {

	// Object
	
	public User(Program program) {
		this.program = program;
		
		skin = new Skin("skin.png");

		main = new MainFrame(program);
		info = new InfoFrame(program);
		icon = new MainIcon(program);

		show(true);
	}
	
	public final Program program;
	public final Skin skin;
	public final MainFrame main;
	public final InfoFrame info;
	public final MainIcon icon;
	
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
}
