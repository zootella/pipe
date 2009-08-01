package base.net.flow;

import base.data.Bay;
import base.data.Bin;
import base.exception.ProgramException;
import base.net.socket.Socket;
import base.size.Range;
import base.state.Close;
import base.state.Receive;
import base.state.Update;

public class SocketBay extends Close {

	/** Put Bay objects around socket so you can upload and download. */
	public SocketBay(Update up, Socket socket) {
		this.up = up;
		this.socket = socket;
		
		uploadBay = new Bay();
		downloadBay = new Bay();
		
		update = new Update(new MyReceive());
		uploadValve = new UploadValve(update, socket, Range.unlimited());
		downloadValve = new DownloadValve(update, socket, Range.unlimited());
		update.send();
	}
	
	private final Update up;
	private final Update update;
	
	private final Bay uploadBay;
	private final Bay downloadBay;
	
	public UploadValve uploadValve;
	public DownloadValve downloadValve;
	public Socket socket;

	@Override public void close() {
		if (already()) return;
		close(uploadValve);
		close(downloadValve);
		close(socket);
	}
	
	private class MyReceive implements Receive {
		public void receive() throws Exception {
			if (closed()) return;
			try {

				uploadValve.stop();
				downloadValve.stop();
				
				if (uploadBay.hasData() && uploadValve.in() != null && uploadValve.in().hasSpace()) {
					uploadValve.in().add(uploadBay);
					up.send();
				}

				if (downloadBay.size() < Bin.big && downloadValve.out() != null && downloadValve.out().hasData()) {
					downloadBay.add(downloadValve.out());
					up.send();
				}
				
				uploadValve.start();
				downloadValve.start();

			} catch (ProgramException e) { exception = e; close(me()); }
		}
	}
	private SocketBay me() { return this; }
	
	/** Add data to upload to this Bay. */
	public Bay upload() { if (exception != null) throw exception; update.send(); return uploadBay; } // Send update to notice what the caller adds to upload
	/** Get the data we've downloaded here. */
	public Bay download() { if (exception != null) throw exception; update.send(); return downloadBay; }
	
	/** The ProgramException that closed us, or null. */
	public ProgramException exception() { return exception; }
	private ProgramException exception;

	/** After taking our socket and valves, call take() before close(). */ 
	public void take() {
		uploadValve = null; // Forget we had these so close() doesn't close them
		downloadValve = null;
		socket = null;
	}
}
