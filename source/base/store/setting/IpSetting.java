package base.store.setting;

import base.data.Outline;
import base.exception.DataException;
import base.internet.name.Ip;

public class IpSetting {

	public IpSetting(Outline outline, String name, Ip preset) {
		setting = new StringSetting(outline, name, preset.toString());
		this.preset = preset;
	}
	private final StringSetting setting;
	private final Ip preset;
	
	public void set(Ip value) { setting.set(value.toString()); }
	public Ip value() {
		try {
			return new Ip(setting.value());
		} catch (DataException e) { return preset; }
	}
}
