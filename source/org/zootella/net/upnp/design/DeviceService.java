package org.zootella.net.upnp.design;

import java.util.Iterator;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.zootella.data.Outline;
import org.zootella.process.Mistake;
import org.zootella.state.Update;

public class DeviceService {
	
	public DeviceService(Update update) {
		this.update = update;
		listener = new MyDeviceChangeListener();
	}
	
	private final Update update;
	public final DeviceChangeListener listener;
	
	private volatile Device device;
	private volatile Service service;
	private volatile Outline name;
	
	public Device device() { return device; }
	public Service service() { return service; }
	public Outline name() { return name; }

	private class MyDeviceChangeListener implements DeviceChangeListener {

		public void deviceAdded(Device gatewayDevice) {//a pool thread calls in here
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
											
											device = gatewayDevice;
											service = s;

											name = new Outline();
											name.add("", device.getDescriptionFilePath());
											name.add("", device.getDeviceType());
											name.add("", device.getFriendlyName());
											name.add("", device.getManufacture());
											name.add("", device.getManufactureURL());
											name.add("", device.getModelDescription());
											name.add("", device.getModelName());
											name.add("", device.getModelNumber());
											name.add("", device.getModelURL());
											name.add("", device.getSerialNumber());
											name.add("", device.getUDN());
											name.add("", device.getUPC());
											name.add("", device.getPresentationURL());
											name.add("", device.getInterfaceAddress());
											name.add("", device.getLocation());
											name.add("", device.getURLBase());
											System.out.println(name.toString());
											
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
