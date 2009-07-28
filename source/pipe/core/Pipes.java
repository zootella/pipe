package pipe.core;

import java.util.ArrayList;
import java.util.List;

import pipe.core.museum.Pipe;
import pipe.main.Program;
import pipe.user.ExchangeDialog;
import pipe.user.FolderDialog;
import pipe.user.MuseumDialog;
import base.state.Close;

public class Pipes extends Close {
	
	public Pipes(Program program) {
		this.program = program;
		
		
		pipes = new ArrayList<Pipe>();
		
	}

	public final Program program;
	
	public final List<Pipe> pipes;

	public void make() {
		
		Pipe pipe = MuseumDialog.show(program);
		if (pipe == null) return;
		
		FolderDialog.show(program, pipe);
		if (!pipe.hasFolder()) { close((Close)pipe); return; }
		
		ExchangeDialog.show(program, pipe);
		if (!pipe.hasAway()) { close((Close)pipe); return; }
		
		pipes.add(pipe);
		program.user.main.fill();
		pipe.go();
	}
	
	public void kill(Pipe pipe) {
		if (pipe == null || !pipes.contains(pipe))
			throw new IllegalArgumentException();
		
		pipes.remove(pipe);
		close((Close)pipe);
		
		program.user.main.fill();
	}

	@Override public void close() {
		if (already()) return;
		
		for (Pipe pipe : pipes)
			close((Close)pipe);
	}
	

}
