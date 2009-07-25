package base.store.setting;

import base.data.Data;
import base.data.Outline;

public class StringSetting {
	
	public StringSetting(Outline outline, String name, String preset) {
		setting = new DataSetting(outline, name, new Data(preset));
	}
	private final DataSetting setting;
	
	public void set(String value) { setting.set(new Data(value)); }
	public String value() { return setting.value().toString(); }
}
