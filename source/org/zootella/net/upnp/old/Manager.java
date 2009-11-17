package org.zootella.net.upnp.old;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Random;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.zootella.net.name.Ip;
import org.zootella.net.name.IpPort;
import org.zootella.net.name.Port;
import org.zootella.net.upnp.name.Map;
import org.zootella.process.Mistake;
import org.zootella.state.Close;

/**
 * Manages the mapping of ports to limewire on UPnP-enabled routers.
 * 
 * According to the UPnP Standards, Internet Gateway Devices must have a
 * specific hierarchy. The parts of that hierarchy that we care about are:
 * 
 * Device: urn:schemas-upnp-org:device:InternetGatewayDevice:1 SubDevice:
 * urn:schemas-upnp-org:device:WANDevice:1 SubDevice:
 * urn:schemas-upnp-org:device:WANConnectionDevice:1 Service:
 * urn:schemas-upnp-org:service:WANIPConnection:1
 * 
 * Every port mapping is a tuple of: - External address ("" is wildcard) -
 * External port - Internal address - Internal port - Protocol (TCP|UDP) -
 * Description
 * 
 * Port mappings can be removed, but that is a blocking network operation which
 * will slow down the shutdown process of Limewire. It is safe to let port
 * mappings persist between limewire sessions. In the meantime however, the NAT
 * may assign a different ip address to the local node. That's why we need to
 * find any previous mappings the node has created and update them with our new
 * address. In order to uniquely distinguish which mappings were made by us, we
 * put part of our client GUID in the description field.
 * 
 * For the TCP mapping, we use the following description:
 * "Lime/TCP:<cliengGUID>" For the UDP mapping, we use "Lime/UDP:<clientGUID>"
 * 
 * NOTES:
 * 
 * Not all NATs support mappings with different external port and internal
 * ports. Therefore if we were unable to map our desired port but were able to
 * map another one, we should pass this information on to Acceptor.
 * 
 * Some buggy NATs do not distinguish mappings by the Protocol field. Therefore
 * we first map the UDP port, and then the TCP port since it is more important
 * should the first mapping get overwritten.
 * 
 * The cyberlink library uses an internal thread that tries to discover any UPnP
 * devices. After we discover a router or give up on trying to, we should call
 * stop().
 */
public class Manager extends Close {

	private static final String gatewayDevice     = "urn:schemas-upnp-org:device:InternetGatewayDevice:1";
	private static final String wanDevice         = "urn:schemas-upnp-org:device:WANDevice:1";
	private static final String connectionDevice  = "urn:schemas-upnp-org:device:WANConnectionDevice:1";
	private static final String connectionService = "urn:schemas-upnp-org:service:WANIPConnection:1";

	private ControlPoint control;
	private Device device;
	private Service service;

	private Map forwardTcp;
	private Map forwardUdp;

	public Manager() {
		//TODO GetControlPointTask
		control = new ControlPoint();
		control.addDeviceChangeListener(new MyDeviceChangeListener());
		control.start(); //TODO catch the exception it might throw
	}

	@Override public void close() {
		if (already()) return;

		if (forwardTcp != null)
			removeMapping(forwardTcp);
		if (forwardUdp != null)
			removeMapping(forwardUdp);

		try {
			if (control != null)
				control.stop();
		} catch (Throwable t) { Mistake.log(t); }
	}

	public Ip getNATAddress() throws UnknownHostException {

		if (!(device != null && service != null))
			return null;

		Action a = getActionFromService(service, "GetExternalIPAddress");
		if (a == null)
			return null;
		if (!a.postControlAction())
			return null;

		Argument r = a.getOutputArgumentList().getArgument("NewExternalIPAddress");
		return new Ip(InetAddress.getByName(r.getValue()));
	}

	public Port mapPort(Port port, Ip ip) {
		// adds a mapping on the router to the specified port
		// returns the external port that was actually mapped. 0 if failed

		Random gen = null;
		
		String localIp = ip.toString();
		Port localPort = port;

		// try adding new mappings with the same port
		Map udp = new Map(port, new IpPort(new Ip(localIp), localPort), "UDP", "LimeUDP");

		// add udp first in case it gets overwritten.
		// if we can't add, update or find an appropriate port
		// give up after 20 tries
		int tries = 20;
		while (!addMapping(udp)) {
			if (tries < 0)
				break;
			tries--;

			// try a random port
			if (gen == null)
				gen = new Random();
			port = new Port(gen.nextInt(50000) + 2000);
			udp = new Map(port, new IpPort(new Ip(localIp), localPort), "UDP", "LimeUDP");
		}

		if (tries < 0)
			return new Port(0);

		// at this stage, the variable port will point to the port the UDP mapping
		// got mapped to. Since we have to have the same port for UDP and tcp,
		// we can't afford to change the port here. So if mapping to this port on tcp
		// fails, we give up and clean up the udp mapping.
		// Note: Phillipe reported that on some routers adding an UDP mapping will also
		// create a TCP mapping. So we no longer delete the UDP mapping if the TCP one fails.
		Map tcp = new Map(port, new IpPort(new Ip(localIp), localPort), "TCP", "LimeTCP");
		if (!addMapping(tcp))
			tcp = null;

		// save a ref to the mappings
		forwardTcp = tcp;
		forwardUdp = udp;

		return port;
	}

	private boolean addMapping(Map f) {

		Action a = getActionFromService(service, "AddPortMapping");
		if (a == null)
			return false;

		a.setArgumentValue("NewRemoteHost",             f.outsideIp);        // A String
		a.setArgumentValue("NewExternalPort",           f.outsidePort.port); // An int
		a.setArgumentValue("NewInternalClient",         f.inside.ip.toString());
		a.setArgumentValue("NewInternalPort",           f.inside.port.port);
		a.setArgumentValue("NewProtocol",               f.protocol);
		a.setArgumentValue("NewPortMappingDescription", f.description);
		a.setArgumentValue("NewEnabled",                "1"); // A String
		a.setArgumentValue("NewLeaseDuration",          0);   // An int

		return a.postControlAction();
	}

	private boolean removeMapping(Map f) {

		Action a = getActionFromService(service, "DeletePortMapping");
		if (a == null)
			return false;

		a.setArgumentValue("NewRemoteHost",   f.outsideIp);
		a.setArgumentValue("NewExternalPort", f.outsidePort.port);
		a.setArgumentValue("NewProtocol",     f.protocol);

		return a.postControlAction();
	}
	
	

	//TODO have it throw a NetException instead of returning null
	public static Action getActionFromService(Service service, String actionName) {
		
		Action a = service.getAction(actionName);
		if (a != null)
			return a;

		for (Object o : service.getActionList()) {
			if (o instanceof Action) {
				a = (Action)o;
				// see http://forum.limewire.org/showpost.php?p=21952&postcount=1 for an example
				// router that adds whitespace
				if (a.getName() != null && actionName.equals(a.getName().trim()))
					return a;
			}
		}

		return null;
	}


	
	

	private class MyDeviceChangeListener implements DeviceChangeListener {
		
		/** this method will be called when we discover a UPnP device. */
		public void deviceAdded(Device dev) {
			
			if (device != null && service != null)
				return;

			// did we find a router?
			if (dev.getDeviceType().equals(gatewayDevice) && dev.isRootDevice())
				device = dev;
			
			if (device != null) {
				discoverService();
				
				// did we find the service we need?
				if (service == null)
					device = null;
				else
					close(Manager.this);
			}

			if (device != null && service != null);
				//TODO update.send();
		}

		public void deviceRemoved(Device dev) {}
	}

	private void discoverService() {

		for (Iterator<Device> iter = device.getDeviceList().iterator(); iter.hasNext(); ) {
			
			Device d = iter.next();
			if (!d.getDeviceType().equals(wanDevice))
				continue;

			DeviceList l = d.getDeviceList();

			for (int i = 0; i < d.getDeviceList().size(); i++) {
				
				Device current2 = l.getDevice(i);

				if (!current2.getDeviceType().equals(connectionDevice))
					continue;

				service = current2.getService(connectionService);
				return;
			}
		}
	}
}
