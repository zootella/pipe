package pipe.main;

import javax.swing.UIManager;

import pipe.core.Core;
import pipe.user.User;
import base.exception.PlatformException;
import base.process.Mistake;
import base.state.Close;

public class Program extends Close {
	
	public Program() {
		
		// Have Java Swing try to look native
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { throw new PlatformException(e); }

		core = new Core(this); // Make the core that does everything
		user = new User(this); // Put the window on the screen to let the user interact with it
	}
	
	public final Core core;
	public final User user;

	@Override public void close() {
		if (already()) return;
		
		close(user);
		close(core);
		
		Mistake.closeCheck();
	}
}
