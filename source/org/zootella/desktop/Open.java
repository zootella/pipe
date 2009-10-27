package org.zootella.desktop;

import java.awt.Desktop;
import java.net.URI;

import org.zootella.file.Path;
import org.zootella.process.Mistake;

public class Open {

	// -------- Run programs, open files, and browse to Web pages --------

	/** Open the file at the given path, as though the user had double-clicked it on the desktop. */
	public static void file(Path path) {
		try {
			Desktop.getDesktop().open(path.file);
		} catch (Throwable t) { Mistake.log(t); } // Don't do anything if it doesn't work
	}

	/** Open the given Web address in the user's default Web browser. */
	public static void url(URI url) {
		try {
			Desktop.getDesktop().browse(url);
		} catch (Throwable t) { Mistake.log(t); } // Don't do anything if it doesn't work
	}
}
