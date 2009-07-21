package base.desktop;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import base.file.Path;
import base.process.Mistake;

public class Open {

	// -------- Run programs, open files, and browse to Web pages --------

	/** Open the file at the given path, as though the user had double-clicked it on the desktop. */
	public static void file(Path path) {
		try {
			Desktop.getDesktop().open(path.file);
		} catch (IOException e) { Mistake.log(e); } // Don't do anything if it doesn't work
	}

	/** Open the given Web address in the user's default Web browser. */
	public static void url(URI url) {
		try {
			Desktop.getDesktop().browse(url);
		} catch (IOException e) { Mistake.log(e); } // Don't do anything if it doesn't work
	}
}
