package base.store;

import base.data.Number;
import base.data.Outline;
import base.exception.DataException;

public class NumberSetting {
	
	public NumberSetting(Outline outline, String name, long program) {
		setting = new StringSetting(outline, name, Number.toString(program));
		this.program = program;
	}
	private final StringSetting setting;
	private final long program;
	
	public void set(long value) { setting.set(Number.toString(value)); }
	public long value() {
		try {
			return Number.toLong(setting.value());
		} catch (DataException e) { return program; }
	}
}
