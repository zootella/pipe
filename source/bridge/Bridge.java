package bridge;

import java.util.ArrayDeque;
import java.util.Deque;

import base.data.Bin;
import base.data.Data;
import base.data.Split;
import base.internet.name.IpPort;
import base.internet.name.Port;
import base.internet.socket.AcceptTask;
import base.internet.socket.ConnectTask;
import base.internet.socket.DownloadTask;
import base.internet.socket.ListenSocket;
import base.internet.socket.Socket;
import base.internet.socket.UploadTask;
import base.size.Range;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

public class Bridge extends Close {
	
	// Parts
	
	public Bridge() {
		
		uploadBin = Bin.medium();
		downloadBin = Bin.medium();
		uploadMessages = new ArrayDeque<String>();
		downloadMessages = new ArrayDeque<String>();
		
		update = new Update(new MyReceive());
		update.send();
	}

	private Port port;
	private IpPort ipPort;
	
	private ListenSocket listen;
	private AcceptTask accept;
	private ConnectTask connect;
	private Socket socket;

	private UploadTask uploadTask;
	private DownloadTask downloadTask;
	private Bin uploadBin;
	private Bin downloadBin;
	private final Deque<String> uploadMessages;
	private final Deque<String> downloadMessages;
	
	private final Update update;
	private Exception exception;
	
	@Override public void close() {
		if (already()) return;
		
		close(listen);
		close(accept);
		close(connect);
		close(socket);
		close(uploadTask);
		close(downloadTask);
	}

	// Use
	
	public void server(Port port) {
		if (port != null && ipPort != null) throw new IllegalStateException();
		this.port = port;
		update.send();
	}
	
	public void client(IpPort ipPort) {
		if (port != null && ipPort != null) throw new IllegalStateException();
		this.ipPort = ipPort;
		update.send();
	}

	public void send(String s) {
		if ((new Data(s)).size() + 1 > Bin.medium) throw new IndexOutOfBoundsException();
		uploadMessages.add(s);
		update.send();
	}
	public String receive() {
		return downloadMessages.poll();
	}
	
	// Run

	private class MyReceive implements Receive {
		public void receive() {
			if (closed()) return;
			try {

				// Connect server
				if (port != null) {
					if (no(listen))
						listen = new ListenSocket(port); // Make once the first time
					if (no(accept))
						accept = new AcceptTask(update, listen); // Listen for the incoming connection
					if (no(socket) && done(accept)) {// The client connected to us
						socket = accept.result(); // Get the connection socket
						System.out.println("server connected");
					}
				}

				// Connect client
				if (ipPort != null) {
					if (no(socket) && no(connect))
						connect = new ConnectTask(update, ipPort);
					if (no(socket) && done(connect)) {
						socket = connect.result();
						System.out.println("client connected");
					}
				}

				// We uploaded or downloaded some data
				if (done(uploadTask)) {
					uploadTask.result(); // Throw the exception that made us give up
					uploadTask = null;
				}
				if (done(downloadTask)) {
					downloadTask.result();
					downloadTask = null;
				}
				
				// Move text data between list and bins
				if (no(uploadTask)) { // No task is using the upload bin right now
					while (
						uploadMessages.peek() != null && // We have text to upload, and
						(new Data(uploadMessages.peek())).size() + 1 <= uploadBin.space()) { // 
						
						uploadBin.add(new Data(uploadMessages.poll()));
						uploadBin.add(new Data((byte)0));
					}
				}
				if (no(downloadTask)) { // No task is using the download bin right now
					while (true) {
						Split s = downloadBin.data().split((byte)0); // Look for a 0 byte that separates messages
						if (!s.found && downloadBin.isFull()) throw new IndexOutOfBoundsException(); // Message too big
						if (!s.found) break;
						
						String message = s.before.toString();
						
						downloadMessages.add(s.before.toString());
						downloadBin.remove(s.before.size() + 1);
						
						System.out.println("received: " + message);
						//TODO send notification we've changed
					}
				}

				// Upload or download more data
				if (is(socket) && no(uploadTask) && uploadBin.hasData())
					uploadTask = new UploadTask(update, socket, new Range(), uploadBin);
				if (is(socket) && no(downloadTask) && downloadBin.hasSpace())
					downloadTask = new DownloadTask(update, socket, new Range(), downloadBin);

			} catch (Exception e) {
				
				System.out.println("exception:");
				e.printStackTrace();
				
				exception = e;
				close();
			}
		}
	}
}
