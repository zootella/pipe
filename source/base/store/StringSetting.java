package base.store;

import base.data.Data;
import base.data.Outline;

public class StringSetting {
	
	public StringSetting(Outline outline, String name, String preset) {
		setting = new Setting(outline, name, new Data(preset));
	}
	private final Setting setting;
	
	public String value() { return setting.value().toString(); }
	public void set(String value) { setting.set(new Data(value)); }
}
