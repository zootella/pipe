package base.store;

import base.data.Data;
import base.data.Outline;
import base.exception.DataException;
import base.exception.DiskException;
import base.file.File;
import base.file.Path;
import base.process.Mistake;

public class Store {
	
	/** point.txt must be in the present working directory the launcher set for the program when it ran. */
	private static final String point = "point.txt";
	/** In store.folder, store.txt holds the Outline for the next time the program runs. */
	private static final String store = "store.txt";

	/** Load the store outline the program saved the last time it closed. */
	public Store() {

		// Read point.txt in the present working directory to find where the store folder is
		Outline p = Outline.fromText(File.data(Path.work(point)));
		folder = Path.work(p.value("point").toString()); // The value is a relative path
		folder.folderWrite(); // Make it and make sure we can write to it

		// Load the store Outline
		o = new Outline(""); // Make a blank Outline object so o is never null
		try {
			o = Outline.fromText(File.data(folder.add(store))); // Open the store file and parse the text outline inside
		}
		catch (DataException e) { Mistake.log(e); } // That didn't work, o will be the blank empty Outline
		catch (DiskException e) { Mistake.log(e); }
	}
	
	/** Path to the program's folder for application data and settings files. */
	public final Path folder;
	
	/** The program's Outline of settings and data saved to disk between the times it runs. */
	public Outline o;

	/** Save the store Outline for the next time the program starts. */
	public void save() {
		try {
			File.save(folder.add(store), new Data(o.toString()));
		} catch (DiskException e) { Mistake.log(e); }
	}
}
