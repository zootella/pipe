package base.store;

import base.data.Outline;
import base.exception.DataException;

public class BooleanSetting {
	
	public BooleanSetting(Outline outline, String name, boolean program) {
		setting = new StringSetting(outline, name, booleanToString(program));
		this.program = program;
	}
	private final StringSetting setting;
	private final boolean program;
	
	public void set(boolean value) { setting.set(booleanToString(value)); }
	public boolean value() {
		try {
			return stringToBoolean(setting.value());
		} catch (DataException e) { return program; }
	}
	
	// Help
	/*
	private static String booleanToString(boolean b) {
		return b ? "t" : "f";
	}
	
	private static boolean stringToBoolean(String s) {
		if (s.equals("t")) return true;
		if (s.equals("f")) return false;
		throw new DataException();
	}
	*/
}
