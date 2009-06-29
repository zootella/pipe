package pipe.main;

import java.util.ArrayList;
import java.util.List;

import pipe.core.Pipe;
import pipe.user.ExchangeDialog;
import pipe.user.FolderDialog;
import pipe.user.MuseumDialog;

import base.data.Outline;
import base.file.Path;
import base.setting.Store;
import base.state.Close;

/** The core program beneath the window that does everything. */
public class Core extends Close {
	
	// Links
	
	private final Program program;

	// Object
	
	public Core(Program program) {
		this.program = program;
		store = new Store();
		pipes = new ArrayList<Pipe>();

	}
	
	private final Store store;
	
	/** The list of pipes the program is using to transfer files with other computers. */
	public final List<Pipe> pipes;
	

	@Override public void close() {
		if (already()) return;
		
		close(store);
		for (Pipe pipe : pipes)
			pipe.close();
	}
	
	// New

	public void newPipe() {
		
		// Museum dialog
		Pipe pipe = MuseumDialog.show(program);
		if (pipe != null) {

			// Folder dialog
			Path folder = FolderDialog.show(program, pipe.title(), pipe.instruction());
			folder = pipe.folder(folder);
			if (folder != null) {

				// Exchange Dialog
				Outline away = ExchangeDialog.show(program, pipe.home());
				away = pipe.away(away);
				if (away != null) {
					
					// List, show, and start the new pipe
					pipes.add(pipe);
					program.user.frame.fill();
					pipe.go();
				}
			}
		}

		// Close a pipe we made but then didn't add
		if (pipe != null && !pipes.contains(pipe))
			pipe.close();
	}
}
