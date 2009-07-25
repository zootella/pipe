package base.store.setting;

import base.data.Number;
import base.data.Outline;
import base.exception.DataException;

public class NumberSetting {
	
	public NumberSetting(Outline outline, String name, long preset) {
		setting = new StringSetting(outline, name, Number.toString(preset));
		this.preset = preset;
	}
	private final StringSetting setting;
	private final long preset;
	
	public void set(long value) { setting.set(Number.toString(value)); }
	public long value() {
		try {
			return Number.toLong(setting.value());
		} catch (DataException e) { return preset; }
	}
}
