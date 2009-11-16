package org.zootella.net.upnp;

import java.util.Iterator;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.zootella.data.Outline;
import org.zootella.process.Mistake;
import org.zootella.state.Update;

public class Change {
	
	public Change(Update update) {
		this.update = update;
		listener = new MyDeviceChangeListener();
	}
	
	private final Update update;
	public final DeviceChangeListener listener;
	
	private volatile Router router;
	public Router router() { return router; }

	private class MyDeviceChangeListener implements DeviceChangeListener {

		public void deviceAdded(Device gatewayDevice) {//a pool thread calls in here
			try {
				if (router == null) {
					
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
											
											Outline o = new Outline();
											o.add("devicetype",       gatewayDevice.getDeviceType());
											o.add("friendlyname",     gatewayDevice.getFriendlyName()); // Show to the user
											o.add("manufacturer",     gatewayDevice.getManufacture());
											o.add("manufacturerurl",  gatewayDevice.getManufactureURL());
											o.add("modeldescription", gatewayDevice.getModelDescription());
											o.add("modelname",        gatewayDevice.getModelName());
											o.add("modelnumber",      gatewayDevice.getModelNumber());
											o.add("modelurl",         gatewayDevice.getModelURL());
											o.add("serialnumber",     gatewayDevice.getSerialNumber());
											o.add("udn",              gatewayDevice.getUDN());
											o.add("upc",              gatewayDevice.getUPC());
											o.add("presentationurl",  gatewayDevice.getPresentationURL());
											o.add("interfaceaddress", gatewayDevice.getInterfaceAddress());
											o.add("location",         gatewayDevice.getLocation());
											
											router = new Router(gatewayDevice, s, o);
											update.sendFromThread();
											return;
										}
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

	private static final String gatewaySchema    = "urn:schemas-upnp-org:device:InternetGatewayDevice:1";
	private static final String wanSchema        = "urn:schemas-upnp-org:device:WANDevice:1";
	private static final String connectionSchema = "urn:schemas-upnp-org:device:WANConnectionDevice:1";
	private static final String serviceSchema    = "urn:schemas-upnp-org:service:WANIPConnection:1";
}
