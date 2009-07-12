package base.internet.packet;

/** Make a PacketReceive object to have a Packets object show you UDP packets that arrive. */
public interface PacketReceive {

	/** When a packet arrives, the Packets object will call your receive() method, showing the packet to you. */
	public void receive(Packet packet);
}
