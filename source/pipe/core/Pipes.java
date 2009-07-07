package pipe.core;

import java.util.ArrayList;
import java.util.List;

import base.data.Outline;
import base.file.Path;
import base.state.Close;

import pipe.core.museum.Pipe;
import pipe.main.Program;
import pipe.user.ExchangeDialog;
import pipe.user.FolderDialog;
import pipe.user.MuseumDialog;

public class Pipes extends Close {
	
	public Pipes(Program program) {
		this.program = program;
		
		
		pipes = new ArrayList<Pipe>();
		
	}

	public final Program program;
	
	public final List<Pipe> pipes;
	
	
	

	public void make() {
		
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
					program.user.main.fill();
					pipe.go();
				}
			}
		}

		// Close a pipe we made but then didn't add
		if (pipe != null && !pipes.contains(pipe))
			pipe.close();
	}
	
	public void kill(Pipe pipe) {
		if (pipe == null || !pipes.contains(pipe))
			throw new IllegalArgumentException();
		
		pipes.remove(pipe);
		pipe.close();
		
		program.user.main.fill();
	}

	@Override public void close() {
		if (already()) return;
		
		for (Pipe pipe : pipes)
			pipe.close();
	}
	

}
