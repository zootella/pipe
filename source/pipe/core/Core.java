package pipe.core;

import pipe.core.here.Here;
import pipe.main.Program;
import base.internet.name.Port;
import base.internet.packet.Packets;
import base.state.Close;

/** The core program beneath the window that does everything. */
public class Core extends Close {

	public Core(Program program) {
		this.program = program;
		
		
		port = Port.random();

		pipes = new Pipes(program);
		
		packets = new Packets(port);
		
		
		
		
		
		
		here = new Here(port, packets);
	}

	private final Program program;
	private final Port port;
	public final Pipes pipes;
	public final Packets packets;

	public final Here here;
	
	
	@Override public void close() {
		if (already()) return;
		
		close(pipes);
		close(packets);
		close(here);
	}
	
	

	
	

}
