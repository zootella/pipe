package org.zootella.net.upnp;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.zootella.exception.NetException;
import org.zootella.net.name.Ip;
import org.zootella.net.upnp.name.IpResult;
import org.zootella.net.upnp.name.Map;
import org.zootella.net.upnp.name.MapResult;
import org.zootella.time.Now;

public class Do {

	public static ControlPoint start(DeviceChangeListener listen) {
		ControlPoint c = new ControlPoint();
		c.addDeviceChangeListener(listen);
		c.start();
		return c;
	}
	
	public static IpResult ip(Access device) {
		Now start = new Now();
		
		Action a = device.action("GetExternalIPAddress");
		if (a == null) throw new NetException("null action");
		if (!a.postControlAction()) throw new NetException("post false");
		
		Argument r = a.getOutputArgumentList().getArgument("NewExternalIPAddress");
		Ip ip = new Ip(r.getValue());
		return new IpResult(start, ip);
	}

	public static MapResult add(Access router, Map map) {
		Now start = new Now();
		
		Action a = router.action("AddPortMapping");
		if (a == null) throw new NetException("null action");

		a.setArgumentValue("NewRemoteHost",             "");                       // String
		a.setArgumentValue("NewExternalPort",           map.outsidePort.port);     // int
		a.setArgumentValue("NewInternalClient",         map.inside.ip.toString()); // String
		a.setArgumentValue("NewInternalPort",           map.inside.port.port);     // int
		a.setArgumentValue("NewProtocol",               map.protocol);             // String
		a.setArgumentValue("NewPortMappingDescription", map.description);          // String
		a.setArgumentValue("NewEnabled",                "1");                      // String
		a.setArgumentValue("NewLeaseDuration",          0);                        // int
		
		boolean b = a.postControlAction();
		return new MapResult(map, start, b);
	}
	
	public static MapResult remove(Access router, Map map) {
		Now start = new Now();
		
		Action a = router.action("DeletePortMapping");
		if (a == null) throw new NetException("null action");

		a.setArgumentValue("NewRemoteHost",   "");                   // String
		a.setArgumentValue("NewExternalPort", map.outsidePort.port); // int
		a.setArgumentValue("NewProtocol",     map.protocol);         // String

		boolean b = a.postControlAction();
		return new MapResult(map, start, b);
	}
}
