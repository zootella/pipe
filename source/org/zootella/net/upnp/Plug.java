package org.zootella.net.upnp;

import org.zootella.state.Close;

public class Plug extends Close {
	
	public Plug() {
		
	}

	@Override public void close() {
		if (already()) return;
		
	}

}
