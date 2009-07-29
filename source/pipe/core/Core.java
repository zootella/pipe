package pipe.core;

import pipe.core.here.Here;
import pipe.main.Program;
import base.data.Data;
import base.exception.DataException;
import base.net.name.Port;
import base.net.packet.Packets;
import base.process.Mistake;
import base.state.Close;

/** The core program beneath the window that does everything. */
public class Core extends Close {

	public Core(Program program) {
		this.program = program;
		
		Port p = null;
		try {
			p = new Port(program.store.o.o("here").value("port").toString());
		} catch (DataException e) { Mistake.ignore(e); }
		if (p == null) {
			p = Port.random();
			program.store.o.m("here").add("port", new Data(p.toString()));
		}
		port = p;

		pipes = new Pipes(program);
		packets = new Packets(port);
		here = new Here(port, packets);
	}

	private final Program program;
	public final Pipes pipes;
	public final Packets packets;

	private final Port port;
	public final Here here;
	
	
	@Override public void close() {
		if (already()) return;
		
		close(pipes);
		close(packets);
		close(here);
	}
	
	

	
	

}
