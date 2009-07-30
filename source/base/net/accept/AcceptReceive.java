package base.net.accept;

import base.net.flow.SocketBay;

/** Make an AcceptReceive in your object to have Accept show you sockets that have connected to the program. */
public interface AcceptReceive {

	/**
	 * When Accept gets a new incoming socket connection, it will call your AcceptReceive.receive() method, showing it to you.
	 * @return true if you will take socket, false to show it to other parts of the program or let it download some more.
	 */
	public boolean receive(SocketBay socket);
}
