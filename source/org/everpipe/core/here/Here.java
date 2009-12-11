package org.everpipe.core.here;

import org.zootella.data.Outline;
import org.zootella.net.name.Ip;
import org.zootella.net.name.IpPort;
import org.zootella.net.name.Port;
import org.zootella.net.packet.Packets;
import org.zootella.net.upnp.Router;
import org.zootella.net.upnp.name.Map;
import org.zootella.state.Close;
import org.zootella.state.Model;
import org.zootella.state.Receive;
import org.zootella.state.Result;
import org.zootella.state.Update;

public class Here extends Close {

	public Here(Packets packets, Port port) {
		this.packets = packets;
		this.port = port;
		update = new Update(new MyReceive());
		model = new MyModel();
		
		refreshLan();
		/*
		refreshBind();
		refreshNat();
		*/
		refreshCenter();
	}
	
	private final Packets packets;
	private final Port port;
	private final Update update;
	
	private CenterTask centerTask;

	@Override public void close() {
		if (already()) return;
		close(model);
		close(centerTask);
		close(router);
	}
	

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			
			/*
			if (is(router)) {
				if (natModel == null && router.hasName())
					natModel = router.name();
				if (natIp == null && router.hasIp())
					natIp = router.ip();
				if (mapTcp == null && router.hasTcp())
					mapTcp = router.tcp();
				if (mapUdp == null && router.hasUdp())
					mapUdp = router.udp();
			}
			*/

			if (done(centerTask)) {
				centerIp = centerTask.result();
				centerTask = null;
				model.changed();
			}
		}
	}
	
	
	
	
	// summary
	public IpPort net() { return null; }
	public IpPort lan() { return null; }

	// value, time, error
	public Result<Ip>      lanIp()    { return lanIp; }
	public Result<Port>    bindPort() { return bindPort; }
	public Result<Outline> natModel() { return natModel; }
	public Result<Ip>      natIp()    { return natIp; }
	public Result<Map>     mapTcp()   { return mapTcp; }
	public Result<Map>     mapUdp()   { return mapUdp; }
	public Result<Ip>      centerIp() { return centerIp; }
	
	private Result<Ip> lanIp;
	private Result<Port> bindPort;
	private Result<Outline> natModel;
	private Result<Ip> natIp;
	private Result<Map> mapTcp;
	private Result<Map> mapUdp;
	private Result<Ip> centerIp;
	
	// refresh
	public void refreshLan() {
		lanIp = HereLan.ip();
		model.changed();
	}
	public void refreshBind() {}
	public void refreshNat() {
		close(router);
		
		natModel = null;
		natIp = null;
		mapTcp = null;
		mapUdp = null;
		
		IpPort l = new IpPort(HereLan.ip().result(), port);
		Map t = new Map(port, l, "TCP", "Pipe");
		Map u = new Map(port, l, "UDP", "Pipe");
		router = new Router(update, t, u);
		update.send();
	}
	public void refreshCenter() {
		close(centerTask);
		centerTask = new CenterTask(update, packets);
		update.send();
	}
	
	private Router router;

	public final MyModel model;
	public class MyModel extends Model {
		
		public String ips() {
			if (Here.this.net() == null || Here.this.lan() == null) return "";
			return Here.this.net().toString() + " -> " + Here.this.lan().toString();
		}
		
		public String lanIp()    { return describe(Here.this.lanIp()); }
		public String bindPort() { return describe(Here.this.bindPort()); }
		public String natModel() { return describe(Here.this.natModel()); }
		public String natIp()    { return describe(Here.this.natIp()); }
		public String mapTcp()   { return describe(Here.this.mapTcp()); }
		public String mapUdp()   { return describe(Here.this.mapUdp()); }
		public String centerIp() { return describe(Here.this.centerIp()); }

		public String lanIpTime()    { return describeTime(Here.this.lanIp()); }
		public String bindPortTime() { return describeTime(Here.this.bindPort()); }
		public String natModelTime() { return describeTime(Here.this.natModel()); }
		public String natIpTime()    { return describeTime(Here.this.natIp()); }
		public String mapTcpTime()   { return describeTime(Here.this.mapTcp()); }
		public String mapUdpTime()   { return describeTime(Here.this.mapUdp()); }
		public String centerIpTime() { return describeTime(Here.this.centerIp()); }

		public String lanIpError()    { return describeError(Here.this.lanIp()); }
		public String bindPortError() { return describeError(Here.this.bindPort()); }
		public String natModelError() { return describeError(Here.this.natModel()); }
		public String natIpError()    { return describeError(Here.this.natIp()); }
		public String mapTcpError()   { return describeError(Here.this.mapTcp()); }
		public String mapUdpError()   { return describeError(Here.this.mapUdp()); }
		public String centerIpError() { return describeError(Here.this.centerIp()); }
	}
}
