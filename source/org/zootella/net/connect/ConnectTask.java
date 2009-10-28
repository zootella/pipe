package org.zootella.net.connect;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import org.zootella.exception.NetException;
import org.zootella.exception.ProgramException;
import org.zootella.net.name.IpPort;
import org.zootella.net.socket.Socket;
import org.zootella.state.Close;
import org.zootella.state.Task;
import org.zootella.state.TaskBody;
import org.zootella.state.Update;
import org.zootella.time.Now;

public class ConnectTask extends Close {
	
	// Make

	/** Make a new outgoing TCP socket connection to ipPort. */
	public ConnectTask(Update up, IpPort ipPort) {
		this.up = up; // We'll tell above when we're done
		this.ipPort = ipPort;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	public final IpPort ipPort;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}

	// Result
	
	/** The socket we connected, its yours to use and then close, or throws the exception that made us give up. */
	public Socket result() { check(exception, socket); return socket; }
	private ProgramException exception;
	private Socket socket;
	
	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private Socket taskSocket; // References thread() can safely set

		// A separate thread will call this method
		public void thread() {
				
			// Make and connect a new socket to the given IP address and port number
			try {
				Now s = new Now();
				SocketChannel c = SocketChannel.open();
				if (!c.connect(ipPort.toInetSocketAddress())) throw new NetException("false");
				Socket.size(c);
				taskSocket = new Socket(c, ipPort, true, s);
			} catch (IOException e) { throw new NetException(e); }
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			socket = taskSocket;
			close(me());          // We're done
		}
	}
	private ConnectTask me() { return this; } // Give inner code a link to the outer object
}
