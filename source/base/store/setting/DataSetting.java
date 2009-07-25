package base.store.setting;

import base.data.Data;
import base.data.Outline;

public class DataSetting {

	/** Make a Setting object to identify name with factory default preset in outline. */
	public DataSetting(Outline outline, String name, Data preset) {
		this.outline = outline;
		this.name = name;
		this.preset = preset;
		
		if (outline.has(name) && outline.o(name).getData().equals(preset))
			outline.remove(name); // Clear preset from outline
	}
	
	private final Outline outline; // The Outline this setting lives in
	private final String name;
	private final Data preset; // The factory default preset we keep out of outline
	
	public void set(Data value) {
		outline.remove(name); // Clear outline of name
		if (!preset.equals(value)) // Keep preset out of outline
			outline.add(name, value);
	}
	
	public Data value() {
		if (outline.has(name))
			return outline.o(name).getData();
		return preset;
	}
}
