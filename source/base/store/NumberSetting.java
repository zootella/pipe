package base.store;

import base.data.Data;
import base.data.Outline;
import base.exception.DataException;

public class NumberSetting {
	
	public NumberSetting(Outline outline, String name, long program) {
		setting = new DataSetting(outline, name, new Data(program));
		this.program = program;
	}
	private final DataSetting setting;
	private final long program;
	
	public void set(long value) { setting.set(new Data(value)); }
	public long value() {
		try {
			return setting.value().toNumber();
		} catch (DataException e) { return program; }
	}
}
