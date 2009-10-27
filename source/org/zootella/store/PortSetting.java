package org.zootella.store;

import org.zootella.data.Outline;
import org.zootella.exception.DataException;
import org.zootella.net.name.Port;

public class PortSetting {

	public PortSetting(Outline outline, String name, Port program) {
		setting = new StringSetting(outline, name, program.toString());
		this.program = program;
	}
	private final StringSetting setting;
	private final Port program;
	
	public void set(Port value) { setting.set(value.toString()); }
	public Port value() {
		try {
			return new Port(setting.value());
		} catch (DataException e) { return program; }
	}
}
