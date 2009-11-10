package org.zootella.net.upnp.design;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.zootella.data.Outline;

public class UpnpDevice {
	
	public UpnpDevice(Device device, Service service, Outline o) {
		this.device = device;
		this.service = service;
		this.o = o;
	}
	
	public final Device device;
	public final Service service;
	public final Outline o;
}
