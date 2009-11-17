package org.zootella.net.upnp;

import org.zootella.net.name.Ip;
import org.zootella.net.upnp.name.Map;
import org.zootella.state.Close;
import org.zootella.state.Update;

public class Router extends Close {
	
	public Router(Update up, Ip lan) {
		this.up = up;
		this.lan = lan;
	}
	
	private final Update up;
	private final Ip lan;

	@Override public void close() {
		if (already()) return;
		
	}
	
	public boolean ready() {
		return false;
	}
	
	private String name;
	public String name() {
		if (!ready()) throw new IllegalStateException();
		return name;
	}
	
	private Ip outside;
	public Ip outside() {
		if (!ready()) throw new IllegalStateException();
		return outside;
	}
	
	public void add(Map m) {
		if (!ready()) throw new IllegalStateException();
		
	}
	
	public void remove(Map m) {
		if (!ready()) throw new IllegalStateException();
		
	}
	
	

}
