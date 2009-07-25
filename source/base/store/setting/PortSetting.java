package base.store.setting;

import base.data.Outline;
import base.exception.DataException;
import base.internet.name.Port;

public class PortSetting {

	public PortSetting(Outline outline, String name, Port preset) {
		setting = new StringSetting(outline, name, preset.toString());
		this.preset = preset;
	}
	private final StringSetting setting;
	private final Port preset;
	
	public void set(Port value) { setting.set(value.toString()); }
	public Port value() {
		try {
			return new Port(setting.value());
		} catch (DataException e) { return preset; }
	}
}
