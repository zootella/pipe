package pipe.core;

import pipe.main.Program;
import base.internet.packet.Packet;
import base.internet.packet.PacketMachine;
import base.internet.packet.PacketReceive;
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
		packetMachine = new PacketMachine(here.port);
		packetMachine.add(new MyPacketReceive());
		
		
		
		
		
		
		

	}

	private final Program program;
	private final Update update;
	public final Pipes pipes;
	public final PacketMachine packetMachine;
	
	public final Here here;
	
//	private final Time time;

	@Override public void close() {
		if (already()) return;
		
		close(pipes);
		close(packetMachine);
		close(here);
	}
	
	private class MyPacketReceive implements PacketReceive {
		public void receive(Packet packet) {
			
			System.out.println("Packet from " + packet.move.ipPort.toString() + " has data " + packet.bin.data().strike());
			
		}
	}

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {
				

			} catch (Exception e) { Mistake.grab(e); }
		}
	}
}
