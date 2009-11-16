package org.zootella.net.upnp;

import org.zootella.net.name.Port;

public class Forward {

	public Forward(String externalIp, Port externalPort, String internalIp, Port internalPort, String protocol, String description) {
		this.externalIp   = externalIp;
		this.externalPort = externalPort;
		this.internalIp   = internalIp;
		this.internalPort = internalPort;
		this.protocol     = protocol;
		this.description  = description;
	}
	
	public final String externalIp;
	public final Port   externalPort;
	public final String internalIp;
	public final Port   internalPort;
	public final String protocol;
	public final String description;
}
