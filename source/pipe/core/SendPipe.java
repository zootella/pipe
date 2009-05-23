package pipe.core;

import base.file.Path;
import base.state.Close;

public class SendPipe extends Pipe {
	
	public SendPipe(Path folder) {
		this.folder = folder;
		
		
	}
	
	public final Path folder;

	@Override public void close() {
		if (already()) return;
		
	}
}
