package base.store;

import base.data.Data;
import base.data.Outline;

public class DataSetting {

	/** Make a Setting object to identify name with the program default value in outline. */
	public DataSetting(Outline outline, String name, Data program) {
		this.outline = outline;
		this.name = name;
		this.program = program;
		
		if (outline.has(name) && outline.value(name).equals(program))
			outline.remove(name); // Clear the program default from outline
	}
	
	private final Outline outline; // The Outline this setting lives in
	private final String name;
	private final Data program; // The factory default written in this program source code
	
	public void set(Data value) {
		outline.remove(name); // Clear outline of name
		if (!program.equals(value)) // Keep default out of outline
			outline.add(name, value);
	}
	
	public Data value() {
		if (outline.has(name))
			return outline.value(name);
		return program; // Not in outline, use the program default 
	}
}
