package pipe.core;

import pipe.user.ExchangeDialog;
import base.file.Path;
import base.state.Close;

public class SendPipe extends Pipe {
	
	public SendPipe(Path folder) {
		this.folder = folder;
		
		
		/*
		new ExchangeDialog(program, pipe);
		
		if (pipe.readyToStart()) {
			
			program.core.pipes.add(pipe);
			program.core.changed();
			
		} else {
			
			pipe.close();
		}
		*/
		
		
		
		
		
		
		
		
	}
	
	public final Path folder;

	@Override public void close() {
		if (already()) return;
		
	}
}
