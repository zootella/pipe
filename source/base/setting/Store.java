package base.setting;

import base.data.Data;
import base.data.Outline;
import base.exception.DataException;
import base.exception.DiskException;
import base.file.File;
import base.file.Path;
import base.process.Mistake;

public class Store {

	public Store() {
		path = Path.here().add(name);
		
		o = new Outline(""); // Make a blank Outline object so o is never null
		
		try {
			o = Outline.fromText(File.data(path)); // Open the store file and parse the text outline inside
		}
		catch (DataException e) { Mistake.log(e); } // That didn't work, o will be the blank empty Outline
		catch (DiskException e) { Mistake.log(e); }
	}
	
	private static final String name = "store.txt";
	private final Path path;
	public Outline o;
	
	public void save() {
		try {
			File.save(path, new Data(o.toString()));
		} catch (DiskException e) { Mistake.log(e); }
	}
}
