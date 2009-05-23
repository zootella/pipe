package pipe.main;

import base.state.Close;

public class Program extends Close {
	
	public Program() {
		
		core = new Core(); // Make the core that does everything
		user = new User(this); // Put the window on the screen to let the user interact with it
		
	}
	
	public final Core core;
	public final User user;

	@Override public void close() {
		if (already()) return;
		
		close(user);
		close(core);
	}
}
