package org.zootella.net.upnp;

import org.zootella.net.name.Port;

public class Map {

	public Map(String externalIp, Port externalPort, String internalIp, Port internalPort, String protocol, String description) {
		this.externalIp   = externalIp;
		this.externalPort = externalPort;
		this.internalIp   = internalIp;
		this.internalPort = internalPort;
		this.protocol     = protocol;
		this.description  = description;
	}
	
	public final String externalIp; //hardcode this to blank
	public final Port   externalPort;
	public final String internalIp;
	public final Port   internalPort;
	public final String protocol;
	public final String description;
	// add in the other two that you tell to the library
	
	//add an update to this
	//add the success and fail boolean, and a duration of its lifetime
	
	
	//TODO what if here you kept the time we tried, the time we finished, and whether it succeeded or failed
	// so i guess the constructor takes an update that gets send()ed when the result comes back
}
