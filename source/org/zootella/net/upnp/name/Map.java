package org.zootella.net.upnp.name;

import org.zootella.net.name.IpPort;
import org.zootella.net.name.Port;

public class Map {

	public Map(Port outside, IpPort inside, String protocol, String description) {
		this.outsideIp   = "";
		this.outsidePort = outside;
		this.inside      = inside;
		this.protocol    = protocol;
		this.description = description;
		this.enabled     = "1";
		this.duration    = 0;
	}

	public final String outsideIp;
	public final Port outsidePort;
	public final IpPort inside;
	public final String protocol;
	public final String description;
	public final String enabled;
	public final int duration;
}
