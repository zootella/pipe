package org.zootella.net.upnp;

import java.util.Iterator;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.zootella.process.Mistake;
import org.zootella.state.Close;
import org.zootella.time.Duration;
import org.zootella.time.Now;

public class Upnp extends Close {

	private static final String gatewaySchema     = "urn:schemas-upnp-org:device:InternetGatewayDevice:1";
	private static final String wanSchema         = "urn:schemas-upnp-org:device:WANDevice:1";
	private static final String connectionSchema  = "urn:schemas-upnp-org:device:WANConnectionDevice:1";
	private static final String serviceSchema     = "urn:schemas-upnp-org:service:WANIPConnection:1";
	
	public Upnp() {
		Now now = new Now();
		control = new ControlPoint();
		control.addDeviceChangeListener(new MyDeviceChangeListener());
		control.start();
		Duration duration = new Duration(now);
		System.out.println(duration.time() + " milliseconds");//78 milliseconds, that's fine
	}

	private final ControlPoint control;
	private volatile Device device;
	private volatile Service service;
	
	public Device device() { return device; }
	public Service service() { return service; }

	@Override public void close() {
		if (already()) return;
		control.stop();
	}

	private class MyDeviceChangeListener implements DeviceChangeListener {

		public void deviceAdded(Device gatewayDevice) {
			try {
				if (device == null && service == null) {
					
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
											System.out.println("got em");
											
											device = gatewayDevice;
											service = s;
											return;
										}
									}
								}
							}
						}
					}
				}
			} catch (Throwable t) { Mistake.log(t); }
		}

		public void deviceRemoved(Device d) {}
	}
}
