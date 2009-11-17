package org.zootella.net.upnp.name;

import org.zootella.time.Duration;
import org.zootella.time.Now;

public class MapResult {
	
	public MapResult(Map map, Now start, boolean result) {
		this.map = map;
		this.duration = new Duration(start);
		this.result = result;
	}

	public final Map map;
	public final Duration duration;
	public final boolean result;
}
