package pipe.main;

import pipe.core.Core;
import pipe.user.User;
import base.process.Mistake;
import base.state.Close;
import base.store.Store;
import base.user.Face;

public class Program extends Close {
	
	public Program() {

		Face.blend(); // Tell Java how to show the program's user interface

		store = new Store(); // Load data and preferences from last time the program ran
		core = new Core(this); // Make the core that does everything
		user = new User(this); // Put the window on the screen to let the user interact with it
	}
	
	public final Store store;
	public final Core core;
	public final User user;

	@Override public void close() {
		if (already()) return;
		
		close(user);
		close(core);
		store.save();
		Mistake.closeCheck();
	}
}
