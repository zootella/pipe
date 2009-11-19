package org.everpipe.core.here;

import org.everpipe.core.here.old.Here.MyReceive;
import org.zootella.exception.ProgramException;
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
import org.zootella.time.Duration;
import org.zootella.user.Describe;

public class Here extends Close {

	public Here() {

		update = new Update(new MyReceive());
		
		model = new MyModel();
		
	}
	
	private final Update update;
	
	

	@Override public void close() {
		if (already()) return;
	}
	

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;

		}
	}
	
	
	
	
	// summary
	public IpPort net() { return null; }
	public IpPort lan() { return null; }

	// value, time, error
	public Result<Ip>     lanIp() { return null; }
	public Result<Port>   bindPort() { return null; }
	public Result<String> natModel() { return null; }
	public Result<Ip>     natIp() { return null; }
	public Result<String> mapTcp() { return null; }
	public Result<String> mapUdp() { return null; }
	public Result<Ip>     centerIp() { return null; }
	
	// refresh
	public void refreshLan() {}
	public void refreshBind() {}
	public void refreshNat() {}
	public void refreshCenter() {}

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

		public String lanIpError()    { return Here.this.lanIp()    == null ? "" : Here.this.lanIp().duration.toString(); }
		public String bindPortError() { return Here.this.bindPort() == null ? "" : Here.this.bindPort().duration.toString(); }
		public String natModelError() { return Here.this.natModel() == null ? "" : Here.this.natModel().duration.toString(); }
		public String natIpTime()    { return Here.this.natIp()    == null ? "" : Here.this.natIp().duration.toString(); }
		public String mapTcpTime()   { return Here.this.mapTcp()   == null ? "" : Here.this.mapTcp().duration.toString(); }
		public String mapUdpTime()   { return Here.this.mapUdp()   == null ? "" : Here.this.mapUdp().duration.toString(); }
		public String centerIpTime() { return Here.this.centerIp() == null ? "" : Here.this.centerIp().duration.toString(); }

		
		
		
		
		public String internet() { return Describe.object((Here.this.internet())); }
		
		public String age() {
			if (Here.this.age() == null) return "";
			return Here.this.age().toString() + " (" + Describe.timeCoarse(Here.this.age().age()) + " ago)";
		}
		
		public String exception() { return Describe.object((Here.this.exception())); }
	}

}
