package org.everpipe.core;

import org.everpipe.core.here.Here;
import org.everpipe.core.here.HereOld;
import org.everpipe.main.Program;
import org.zootella.data.Data;
import org.zootella.exception.DataException;
import org.zootella.net.accept.Accept;
import org.zootella.net.name.Port;
import org.zootella.net.packet.Packets;
import org.zootella.process.Mistake;
import org.zootella.state.Close;

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
		accept = new Accept(port);
		packets = new Packets(port);
		here = new Here(packets, port);
	}

	private final Program program;
	public final Pipes pipes;
	public final Accept accept;
	public final Packets packets;

	private final Port port;
	public final Here here;
	
	
	@Override public void close() {
		if (already()) return;
		
		close(pipes);
		close(accept);
		close(packets);
		close(here);
	}
	
	

	
	

}
