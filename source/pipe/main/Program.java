package pipe.main;

import java.util.ArrayList;
import java.util.List;

import pipe.core.Pipe;

import base.setting.Store;
import base.state.Close;

/** The core program beneath the window that does everything. */
public class Program extends Close {
	
	// Links
	
	private final Store store;

	// Object
	
	public Program(Store store) {
		this.store = store;
		pipes = new ArrayList<Pipe>();

	}
	
	/** The list of pipes the program is using to transfer files with other computers. */
	public final List<Pipe> pipes;

	@Override public void close() {
		if (already()) return;
		
		close(store);
		
		// Make sure every object with a close() method ran
		Close.checkAll();
	}
}
