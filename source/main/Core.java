package main;

import base.state.Close;

public class Core extends Close {

	// Object
	
	public Core() {

	}

	@Override public void close() {
		if (already()) return;
		
	}
}
