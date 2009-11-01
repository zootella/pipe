package org.zootella.net.accept;

import java.io.IOException;
import java.net.InetSocketAddress;
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

public class AcceptTask extends Close {

	// Make

	/** Wait for a peer to make a TCP socket connection to listen. */
	public AcceptTask(Update up, ListenSocket listen) {
		this.up = up; // We'll tell above when we're done
		this.listen = listen;
		task = new Task(new MyTask()); // Make a separate thread call thread() below now
	}
	
	private final Update up;
	private final ListenSocket listen;
	private final Task task;

	@Override public void close() {
		if (already()) return;
		close(task);
		up.send();
	}
	
	// Result
	
	/** The socket that connected to listen, it's yours to use and then close, or throws the exception that made us give up. */
	public Socket result() { check(exception, socket); return socket; }
	private ProgramException exception;
	private Socket socket;
	
	// Task

	/** Our Task with a thread that runs our code that blocks. */
	private class MyTask implements TaskBody {
		private Socket taskSocket; // References thread() can safely set

		// A separate thread will call this method
		public void thread() {

			// Wait here until a peer connects to us
			try {
				Now s = new Now();
				SocketChannel c = listen.channel.accept();
				if (!c.isConnected()) throw new NetException("false"); // Make sure the given channel is connected
				Socket.size(c);
				IpPort i = new IpPort((InetSocketAddress)c.socket().getRemoteSocketAddress());
				taskSocket = new Socket(c, i, false, s);
			} catch (IOException e) { throw new NetException(e); }
		}

		// Once thread() above returns, the normal event thread calls this done() method
		public void done(ProgramException e) {
			if (closed()) return; // Don't let anything change if we're already closed
			exception = e;        // Get the exception our code above threw
			socket = taskSocket;
			close(AcceptTask.this); // We're done
		}
	}
}
