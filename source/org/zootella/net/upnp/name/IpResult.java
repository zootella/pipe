package org.zootella.net.upnp.name;

import org.zootella.net.name.Ip;
import org.zootella.time.Duration;
import org.zootella.time.Now;

public class IpResult {
	
	public IpResult(Now start, Ip ip) {
		this.duration = new Duration(start);
		this.ip = ip;
	}

	public final Duration duration;
	public final Ip ip;
}
