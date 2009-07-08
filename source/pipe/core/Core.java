package pipe.core;

import pipe.main.Program;
import base.internet.packet.Packet;
import base.internet.packet.PacketMachine;
import base.process.Mistake;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

/** The core program beneath the window that does everything. */
public class Core extends Close {

	public Core(Program program) {
		this.program = program;
		update = new Update(new MyReceive());
		update.send();
		
		here = new Here(program);
		
		pipes = new Pipes(program);
		packet = new PacketMachine(update, here.port);
		
		
		
		
		
		
		

	}

	private final Program program;
	private final Update update;
	public final Pipes pipes;
	public final PacketMachine packet;
	
	public final Here here;
	
//	private final Time time;

	@Override public void close() {
		if (already()) return;
		
		close(pipes);
		close(packet);
		close(here);
	}

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				
				while (packet.receiveHas()) {
					Packet p = packet.receiveLook();
					System.out.println("Packet from " + p.ipPort.toString() + " data " + p.bin.data().base16());
					packet.receiveDone();
				}

			} catch (Exception e) { Mistake.grab(e); }
		}
	}
}
