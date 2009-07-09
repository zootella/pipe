package pipe.core;

import pipe.main.Program;
import base.internet.packet.Packet;
import base.internet.packet.PacketMachine;
import base.internet.packet.PacketReceive;
import base.state.Close;

/** The core program beneath the window that does everything. */
public class Core extends Close {

	public Core(Program program) {
		this.program = program;
		
		Port port = new Port(1234);
		
		
		pipes = new Pipes(program);
		packetMachine = new PacketMachine(port);
		packetMachine.add(new MyPacketReceive());
		here = new Here(packetMachine);
		
		
		
		
		
		
		

	}

	private final Program program;
	public final Pipes pipes;
	public final PacketMachine packetMachine;
	public final Here here;

	@Override public void close() {
		if (already()) return;
		
		close(pipes);
		close(packetMachine);
		close(here);
	}
	
	private class MyPacketReceive implements PacketReceive {
		public void receive(Packet packet) {
			if (closed()) return;
			
			
		}
	}

}
