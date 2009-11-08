package org.zootella.upnp;

import org.zootella.net.name.IpPort;

public class Forward {

	public Forward(IpPort outside, IpPort inside, String protocol, String description) {
		this.outside = outside;
		this.inside = inside;
		this.protocol = protocol;
		this.description = description;
	}
	
	public final IpPort outside;
	public final IpPort inside;
	public final String protocol;
	public final String description;
}
