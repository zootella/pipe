package base.setting;

import base.data.Outline;
import base.exception.DataException;
import base.file.Path;

public class PathSetting {
	
	public PathSetting(Outline outline, String name, Path preset) {
		setting = new StringSetting(outline, name, preset.toString());
		this.preset = preset;
	}
	private final StringSetting setting;
	private final Path preset;
	
	public void set(Path value) { setting.set(value.toString()); }
	public Path value() {
		try {
			return new Path(setting.value());
		} catch (DataException e) { return preset; }
	}
}
