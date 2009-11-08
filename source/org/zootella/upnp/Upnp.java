package org.zootella.upnp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.zootella.data.Data;
import org.zootella.net.name.Ip;
import org.zootella.net.name.IpPort;
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
public class Upnp extends Close {
	
	// Define

	/** some schemas */
	private static final String urnRouterDevice        = "urn:schemas-upnp-org:device:InternetGatewayDevice:1";
	private static final String urnWanDevice           = "urn:schemas-upnp-org:device:WANDevice:1";
	private static final String urnWanConnectionDevice = "urn:schemas-upnp-org:device:WANConnectionDevice:1";
	private static final String urnServiceType         = "urn:schemas-upnp-org:service:WANIPConnection:1";

	/** prefixes and a suffix for the descriptions of our TCP and UDP mappings */
	private static final String tcpPrefix = "LimeTCP";
	private static final String udpPrefix = "LimeUDP";
	
	// Objects

	private final ControlPoint control;
	private Device device;
	private Service service;

	/** The tcp and udp mappings created this session */
	private OldMapping tcpMapping;
	private OldMapping udpMapping;
	
	// Create and close

	public Upnp() {
		System.out.println("Starting UPnP Manager.");
		
		control = new ControlPoint();
		control.addDeviceChangeListener(new MyDeviceChangeListener());

		try {
			control.start();
		} catch (Exception e) {
			System.out.println("error: " + e);
		}
	}

	@Override public void close() {
		if (already()) return;

		try { control.stop(); } catch (Throwable t) { Mistake.log(t); }
	}
	
	// Look

	/** true if we're behind an UPnP NAT device. */
	public boolean isNATPresent() {
		return device != null && service != null;
	}

	/** true if we've created mappings. */
	public boolean mappingsExist() {
		return tcpMapping != null || udpMapping != null;
	}
	
	// API

	/** Blocks and returns the external address the NAT thinks we have, or null can't find it. */
	public InetAddress getNATAddress() throws UnknownHostException {

		if (!isNATPresent())
			return null;

		Action getIP = getActionFromService(service, "GetExternalIPAddress");
		if (getIP == null) {
			System.out.println("Couldn't find GetExternalIPAddress action!");
			return null;
		}

		if (!getIP.postControlAction()) {
			System.out.println("couldn't get our external address");
			return null;
		}

		Argument ret = getIP.getOutputArgumentList().getArgument("NewExternalIPAddress");
		return InetAddress.getByName(ret.getValue());
	}

	/**
	 * adds a mapping on the router to the specified port
	 * 
	 * @return the external port that was actually mapped. 0 if failed
	 */
	public int mapPort(int port, byte[] address) {
		System.out.println("Attempting to map port: " + port);

		Random gen = null;
		
		String localAddress = (new Ip(new Data(address))).toString();
		int localPort = port;

		// try adding new mappings with the same port
		OldMapping udp = new OldMapping("", port, localAddress, localPort, "UDP", udpPrefix + getGUIDSuffix());

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
			port = gen.nextInt(50000) + 2000;
			udp = new OldMapping("", port, localAddress, localPort, "UDP", udpPrefix + getGUIDSuffix());
		}

		if (tries < 0) {
			System.out.println("couldn't map a port :(");
			return 0;
		}

		// at this stage, the variable port will point to the port the UDP mapping
		// got mapped to. Since we have to have the same port for UDP and tcp,
		// we can't afford to change the port here. So if mapping to this port on tcp
		// fails, we give up and clean up the udp mapping.
		// Note: Phillipe reported that on some routers adding an UDP mapping will also
		// create a TCP mapping. So we no longer delete the UDP mapping if the TCP one fails.
		OldMapping tcp = new OldMapping("", port, localAddress, localPort, "TCP", tcpPrefix + getGUIDSuffix());
		if (!addMapping(tcp)) {
			System.out.println(" couldn't map tcp to whatever udp was mapped. leaving udp around...");
			tcp = null;
		}

		// save a ref to the mappings
		tcpMapping = tcp;
		udpMapping = udp;

		return port;
	}

	private boolean addMapping(OldMapping m) {

		System.out.println("adding " + m);

		Action add = getActionFromService(service, "AddPortMapping");

		if (add == null)
			return false;

		add.setArgumentValue("NewRemoteHost", m.externalAddress);
		add.setArgumentValue("NewExternalPort", m.externalPort);
		add.setArgumentValue("NewInternalClient", m.internalAddress);
		add.setArgumentValue("NewInternalPort", m.internalPort);
		add.setArgumentValue("NewProtocol", m.protocol);
		add.setArgumentValue("NewPortMappingDescription", m.description);
		add.setArgumentValue("NewEnabled", "1");
		add.setArgumentValue("NewLeaseDuration", 0);

		boolean success = add.postControlAction();
		System.out.println("Post succeeded: " + success);
		return success;
	}

	private String getGUIDSuffix() {
		return "0123456789";
	}

	/**
	 * @param m
	 *            the mapping to remove from the NAT
	 * @return whether it worked or not
	 */
	private boolean removeMapping(OldMapping m) {

		System.out.println("removing " + m);

		Action remove = getActionFromService(service, "DeletePortMapping");

		if (remove == null) {
			System.out.println("Couldn't find DeletePortMapping action!");
			return false;
		}

		remove.setArgumentValue("NewRemoteHost", m.externalAddress);
		remove.setArgumentValue("NewExternalPort", m.externalPort);
		remove.setArgumentValue("NewProtocol", m.protocol);

		boolean success = remove.postControlAction();
		System.out.println("Remove succeeded: " + success);
		return success;
	}

	/**
	 * Gets an action from a service, trimming whitespace if necessary. see:
	 * http://forum.limewire.org/showpost.php?p=21952&postcount=1 for an example
	 * router that adds whitespace
	 */
	private Action getActionFromService(Service serviceWithAction, String actionName) {
		Action action = serviceWithAction.getAction(actionName);
		if (action != null) {
			return action;
		}

		System.out.println("Couldn't find action: " + actionName + ", from direct lookup");

		for (Object actionObj : service.getActionList()) {
			if (actionObj instanceof Action) {
				action = (Action) actionObj;
				if (action.getName() != null
						&& actionName.equals(action.getName().trim())) {
					return action;
				}
			}
		}

		System.out.println("Couldn't find action: " + actionName + " after iterating");

		return null;
	}

	/**
	 * schedules a shutdown hook which will clear the mappings created this
	 * session.
	 */
	public void clearMappings() {
		System.out.println("start cleaning");
		
		if (tcpMapping != null)
			removeMapping(tcpMapping);
		if (udpMapping != null)
			removeMapping(udpMapping);
		
		System.out.println("done cleaning");
	}

	private class MyDeviceChangeListener implements DeviceChangeListener {
		
		/** this method will be called when we discover a UPnP device. */
		public void deviceAdded(Device dev) {
			
			if (isNATPresent())
				return;
			
			System.out.println("Device added: " + dev.getFriendlyName());
			
			// did we find a router?
			if (dev.getDeviceType().equals(urnRouterDevice) && dev.isRootDevice())
				device = dev;
			
			if (device != null) {
				
				discoverService();
				
				// did we find the service we need?
				if (service == null) {
					System.out.println("couldn't find service");
					device = null;
				} else {
					System.out.println("Found service, router: " + device.getFriendlyName() + ", service: " + service);
					close(Upnp.this);
				}
			} else {
				System.out.println("didn't get router device");
			}

			if (isNATPresent()) {
				//Here's where we update.send();
				/*
				for (UPnPListener listener : listeners)
					listener.natFound();
					*/
			}
		}

		public void deviceRemoved(Device dev) {}
	}

	/**
	 * Traverses the structure of the router device looking for the port mapping
	 * service.
	 */
	private void discoverService() {

		for (Iterator<Device> iter = device.getDeviceList().iterator(); iter.hasNext(); ) {
			
			Device current = iter.next();
			if (!current.getDeviceType().equals(urnWanDevice))
				continue;

			DeviceList l = current.getDeviceList();
			System.out.println("found " + current.getDeviceType() + ", size: " + l.size() + ", on: " + current.getFriendlyName());

			for (int i = 0; i < current.getDeviceList().size(); i++) {
				
				Device current2 = l.getDevice(i);

				if (!current2.getDeviceType().equals(urnWanConnectionDevice))
					continue;

				System.out.println("found " + current2.getDeviceType() + ", on: " + current2.getFriendlyName());

				service = current2.getService(urnServiceType);
				return;
			}
		}
	}

	private final class OldMapping {
		
		public final String externalAddress;
		public final int    externalPort;
		public final String internalAddress;
		public final int    internalPort;
		public final String protocol;
		public final String description;

		// internal constructor
		public OldMapping(String externalAddress, int externalPort, String internalAddress, int internalPort, String protocol, String description) {
			this.externalAddress = externalAddress;
			this.externalPort    = externalPort;
			this.internalAddress = internalAddress;
			this.internalPort    = internalPort;
			this.protocol        = protocol;
			this.description     = description;
		}
	}
}
