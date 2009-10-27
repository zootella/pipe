package org.zootella.store;

import org.zootella.data.Outline;
import org.zootella.exception.DataException;
import org.zootella.net.name.Ip;

public class IpSetting {

	public IpSetting(Outline outline, String name, Ip program) {
		setting = new StringSetting(outline, name, program.toString());
		this.program = program;
	}
	private final StringSetting setting;
	private final Ip program;
	
	public void set(Ip value) { setting.set(value.toString()); }
	public Ip value() {
		try {
			return new Ip(setting.value());
		} catch (DataException e) { return program; }
	}
}
