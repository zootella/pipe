package bridge;

import base.data.Bay;
import base.data.Bin;
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
	
	public Bridge(Update up) {
		this.up = up;
		
		uploadBin = Bin.medium();
		downloadBin = Bin.medium();
		uploadBay = new Bay();
		downloadBay = new Bay();
		
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
	private final Bay uploadBay;
	private final Bay downloadBay;
	
	private final Update up;
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
		uploadBay.add(s);
		uploadBay.add((byte)0);
		update.send();
	}
	public String receive() {
		Split split = downloadBay.data().split((byte)0);
		if (!split.found) return null;
		String s = split.before.toString();
		downloadBay.remove(split.before.size() + 1);
		return s;
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
					if (no(socket) && done(accept)) { // The client connected to us
						socket = accept.result(); // Get the connection socket
						up.send();
					}
				}

				// Connect client
				if (ipPort != null) {
					if (no(socket) && no(connect))
						connect = new ConnectTask(update, ipPort);
					if (no(socket) && done(connect)) {
						socket = connect.result();
						up.send();
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
				
				// Move data between bays and bins
				if (uploadBay.hasData() && no(uploadTask) && uploadBin.hasSpace())
					uploadBin.add(uploadBay);
				if (no(downloadTask) && downloadBin.hasData()) {
					downloadBay.add(downloadBin);
					up.send();
				}
				
				// Upload and download more data
				if (is(socket) && no(uploadTask) && uploadBin.hasData())
					uploadTask = new UploadTask(update, socket, new Range(), uploadBin);
				if (is(socket) && no(downloadTask) && downloadBin.hasSpace())
					downloadTask = new DownloadTask(update, socket, new Range(), downloadBin);

			} catch (Exception e) {
				exception = e;
				close();
				up.send();
			}
		}
	}
}
