package org.everpipe.core.here;

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
		refreshBind();
		refreshNat();
		refreshCenter();
	}
	
	private final Packets packets;
	private final Port port;
	private final Update update;
	
	private CenterTask centerTask;
	
	

	@Override public void close() {
		if (already()) return;
	}
	

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			
			if (done(router)) {
				
			}

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
	public Result<Ip>     lanIp()    { return lanIp; }
	public Result<Port>   bindPort() { return bindPort; }
	public Result<String> natModel() { return natModel; }
	public Result<Ip>     natIp()    { return natIp; }
	public Result<String> mapTcp()   { return mapTcp; }
	public Result<String> mapUdp()   { return mapUdp; }
	public Result<Ip>     centerIp() { return centerIp; }
	
	private Result<Ip> lanIp;
	private Result<Port> bindPort;
	private Result<String> natModel;
	private Result<Ip> natIp;
	private Result<String> mapTcp;
	private Result<String> mapUdp;
	private Result<Ip> centerIp;
	
	// refresh
	public void refreshLan() {
		lanIp = HereLan.ip();
	}
	public void refreshBind() {}
	public void refreshNat() {
		close(router);
		IpPort l = new IpPort(HereLan.ip().result, port);
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
		
		public String lanIp()    { return Here.this.lanIp()    == null ? "" : Here.this.lanIp().result.toString(); }
		public String bindPort() { return Here.this.bindPort() == null ? "" : Here.this.bindPort().result.toString(); }
		public String natModel() { return Here.this.natModel() == null ? "" : Here.this.natModel().result.toString(); }
		public String natIp()    { return Here.this.natIp()    == null ? "" : Here.this.natIp().result.toString(); }
		public String mapTcp()   { return Here.this.mapTcp()   == null ? "" : Here.this.mapTcp().result.toString(); }
		public String mapUdp()   { return Here.this.mapUdp()   == null ? "" : Here.this.mapUdp().result.toString(); }
		public String centerIp() { return Here.this.centerIp() == null ? "" : Here.this.centerIp().result.toString(); }

		public String lanIpTime()    { return Here.this.lanIp()    == null ? "" : Here.this.lanIp().duration.toString(); }
		public String bindPortTime() { return Here.this.bindPort() == null ? "" : Here.this.bindPort().duration.toString(); }
		public String natModelTime() { return Here.this.natModel() == null ? "" : Here.this.natModel().duration.toString(); }
		public String natIpTime()    { return Here.this.natIp()    == null ? "" : Here.this.natIp().duration.toString(); }
		public String mapTcpTime()   { return Here.this.mapTcp()   == null ? "" : Here.this.mapTcp().duration.toString(); }
		public String mapUdpTime()   { return Here.this.mapUdp()   == null ? "" : Here.this.mapUdp().duration.toString(); }
		public String centerIpTime() { return Here.this.centerIp() == null ? "" : Here.this.centerIp().duration.toString(); }

		public String lanIpError()    { return Here.this.lanIp()    == null ? "" : Here.this.lanIp().exception.toString(); }
		public String bindPortError() { return Here.this.bindPort() == null ? "" : Here.this.bindPort().exception.toString(); }
		public String natModelError() { return Here.this.natModel() == null ? "" : Here.this.natModel().exception.toString(); }
		public String natIpError()    { return Here.this.natIp()    == null ? "" : Here.this.natIp().exception.toString(); }
		public String mapTcpError()   { return Here.this.mapTcp()   == null ? "" : Here.this.mapTcp().exception.toString(); }
		public String mapUdpError()   { return Here.this.mapUdp()   == null ? "" : Here.this.mapUdp().exception.toString(); }
		public String centerIpError() { return Here.this.centerIp() == null ? "" : Here.this.centerIp().exception.toString(); }
	}
}
