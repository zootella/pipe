package main;

import base.state.Close;
import bridge.Bridge;

public class Core extends Close {
	
	
	// Make
	
	public Core() {
		
		bridge = new Bridge();
		
		
	}
	
	// Parts
	
	public final Bridge bridge;

	// Close
	
	@Override public void close() {
		if (already()) return;
		
	}

}
