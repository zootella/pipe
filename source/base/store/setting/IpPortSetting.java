package base.store.setting;

import base.data.Outline;
import base.exception.DataException;
import base.internet.name.IpPort;

public class IpPortSetting {

	public IpPortSetting(Outline outline, String name, IpPort preset) {
		setting = new StringSetting(outline, name, preset.toString());
		this.preset = preset;
	}
	private final StringSetting setting;
	private final IpPort preset;
	
	public void set(IpPort value) { setting.set(value.toString()); }
	public IpPort value() {
		try {
			return new IpPort(setting.value());
		} catch (DataException e) { return preset; }
	}
}
