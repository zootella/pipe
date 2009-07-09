package base.internet.packet;

/** Make a PacketReceive object to have a PacketMachine show you UDP packets that arrive. */
public interface PacketReceive {

	/** When a packet arrives, the PacketMachine will call your receive() method, showing the packet to you. */
	public void receive(Packet packet);
}
