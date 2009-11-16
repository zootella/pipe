package org.zootella.net.upnp;

import java.util.Iterator;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.zootella.net.name.Ip;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.time.Now;
import org.zootella.time.Slow;

public class OldUpnp extends Close {

	private static final String gatewaySchema     = "urn:schemas-upnp-org:device:InternetGatewayDevice:1";
	private static final String wanSchema         = "urn:schemas-upnp-org:device:WANDevice:1";
	private static final String connectionSchema  = "urn:schemas-upnp-org:device:WANConnectionDevice:1";
	private static final String serviceSchema     = "urn:schemas-upnp-org:service:WANIPConnection:1";
	
	public OldUpnp() {//make it a task
		Now now = new Now();
		control = new ControlPoint();
		control.addDeviceChangeListener(new MyDeviceChangeListener());
		control.start();
		new Slow(now);
	}

	private final ControlPoint control;
	private volatile Device device;
	private volatile Service service;
	
	public boolean connected() {
		return device != null && service != null;
	}

	@Override public void close() {
		if (already()) return;
		control.stop();
	}

	private class MyDeviceChangeListener implements DeviceChangeListener {

		public void deviceAdded(Device gatewayDevice) {//a pool thread calls in here
			try {
				if (closed()) return;
				if (connected()) return;

				if (gatewayDevice.getDeviceType().equals(gatewaySchema) && gatewayDevice.isRootDevice()) {
					
					Iterator<Device> iterator = gatewayDevice.getDeviceList().iterator();
					while (iterator.hasNext()) {
						Device wanDevice = iterator.next();
						
						if (wanDevice.getDeviceType().equals(wanSchema)) {
							
							DeviceList list = wanDevice.getDeviceList();
							for (int i = 0; i < wanDevice.getDeviceList().size(); i++) {
								Device connectionDevice = list.getDevice(i);
								
								if (connectionDevice.getDeviceType().equals(connectionSchema)) {
									
									Service s = connectionDevice.getService(serviceSchema);
									if (s != null) {
										System.out.println("found " + gatewayDevice.getFriendlyName());
										
										device = gatewayDevice;
										service = s;
										return;
									}
								}
							}
						}
					}
				}
			} catch (Throwable t) { Mistake.stop(t); }
		}

		public void deviceRemoved(Device d) {}
	}
	
	
	
	
	
	
	public void snippet() {
		Now now = new Now();
		Ip ip = getExternalIp();
		new Slow(now);
		if (ip == null)
			System.out.println("null external ip");
		else
			System.out.println(ip.toString());
	}
	

	public Ip getExternalIp() {//make it a task
		if (service == null)
			return null;

		Action a = action("GetExternalIPAddress");
		if (a == null)
			return null;
		if (!a.postControlAction())
			return null;

		Argument r = a.getOutputArgumentList().getArgument("NewExternalIPAddress");
		return new Ip(r.getValue());
	}	
	
	

	public Action action(String name) {

		Action a = service.getAction(name);
		if (a != null)
			return a;

		for (Object o : service.getActionList()) {
			if (o instanceof Action) {
				a = (Action)o;
				if (a.getName() != null && a.getName().trim().equals(name))
					return a;
			}
		}

		return null;
	}

	
	
	
	
	
	
	
	
	
	
}
