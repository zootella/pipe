package base.setting;

import base.data.Outline;
import base.exception.DataException;

public class BooleanSetting {
	
	public BooleanSetting(Outline outline, String name, boolean preset) {
		setting = new StringSetting(outline, name, booleanToString(preset));
		this.preset = preset;
	}
	private final StringSetting setting;
	private final boolean preset;
	
	public void set(boolean value) { setting.set(booleanToString(value)); }
	public boolean value() {
		try {
			return stringToBoolean(setting.value());
		} catch (DataException e) { return preset; }
	}
	
	// Help
	
	private static String booleanToString(boolean b) {
		return b ? "t" : "f";
	}
	
	private static boolean stringToBoolean(String s) {
		if (s.equals("t")) return true;
		if (s.equals("f")) return false;
		throw new DataException();
	}
}
