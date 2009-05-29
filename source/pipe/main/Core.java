package pipe.main;

import java.util.ArrayList;
import java.util.List;

import pipe.core.Pipe;

import base.setting.Store;
import base.state.Close;

/** The core program beneath the window that does everything. */
public class Core extends Close {
	
	// Links
	
	private final Store store;

	// Object
	
	public Core() {
		store = new Store();
		pipes = new ArrayList<Pipe>();

	}
	
	/** The list of pipes the program is using to transfer files with other computers. */
	public final List<Pipe> pipes;

	@Override public void close() {
		if (already()) return;
		
		close(store);
		for (Pipe pipe : pipes)
			pipe.close();
	}
	
	
	
	
	// call after you add or remove a pipe or one changes
	public void changed() {
		
		
		
	}
	
}
