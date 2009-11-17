package org.zootella.net.upnp;

import org.zootella.net.name.IpPort;
import org.zootella.net.name.Port;
import org.zootella.state.Update;
import org.zootella.time.Duration;
import org.zootella.time.Now;

public class Map {

	public Map(Update update, Port outside, IpPort inside, String protocol, String description) {
		this.start = new Now();
		this.update = update;
		
		this.outsideIp   = "";
		this.outsidePort = outside;
		this.inside      = inside;
		this.protocol    = protocol;
		this.description = description;
		this.enabled     = "1";
		this.duration    = 0;
	}

	public final Now start;
	private final Update update;
	
	public final String outsideIp;
	public final Port outsidePort;
	public final IpPort inside;
	public final String protocol;
	public final String description;
	public final String enabled;
	public final int duration;

	private Duration make;
	public Duration make() { return make; }

	private Boolean result;
	public boolean result() { return result; }
	
	public void result(boolean result) {
		this.make = new Duration(start);
		this.result = result;
		update.send();
	}
}
