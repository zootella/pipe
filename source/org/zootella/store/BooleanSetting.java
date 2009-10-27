package org.zootella.store;

import org.zootella.data.Data;
import org.zootella.data.Outline;
import org.zootella.exception.DataException;

public class BooleanSetting {
	
	public BooleanSetting(Outline outline, String name, boolean program) {
		setting = new DataSetting(outline, name, new Data(program));
		this.program = program;
	}
	private final DataSetting setting;
	private final boolean program;
	
	public void set(boolean value) { setting.set(new Data(value)); }
	public boolean value() {
		try {
			return setting.value().toBoolean();
		} catch (DataException e) { return program; }
	}
}
