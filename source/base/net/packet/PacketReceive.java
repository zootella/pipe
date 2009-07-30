package base.net.packet;

/** Make a PacketReceive in your object to have the Packets object show you UDP packets that arrive. */
public interface PacketReceive {

	/**
	 * When a packet arrives, the Packets object will call your receive() method, showing the packet to you.
	 * @return true if you got this packet, false if it's for some other part of the program
	 */
	public boolean receive(Packet packet);
}
