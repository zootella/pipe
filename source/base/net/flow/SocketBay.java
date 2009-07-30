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
		
		upload = new Bay();
		download = new Bay();
		
		update = new Update(new MyReceive());
		uploadValve = new UploadValve(update, socket, Range.unlimited());
		downloadValve = new DownloadValve(update, socket, Range.unlimited());
		update.send();
	}
	
	private final Update up;
	private final Update update;
	
	public UploadValve uploadValve;
	public DownloadValve downloadValve;
	public Socket socket;
	
	/** Add data to upload to this Bay. */
	public final Bay upload;
	/** Get the data we've downloaded here. */
	public final Bay download;

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
				
				if (upload.hasData() && uploadValve.in() != null && uploadValve.in().hasSpace()) {
					uploadValve.in().add(upload);
					up.send();
				}

				if (download.size() < Bin.big && downloadValve.out() != null && downloadValve.out().hasData()) {
					download.add(downloadValve.out());
					up.send();
				}
				
				uploadValve.start();
				downloadValve.start();

			} catch (ProgramException e) { exception = e; close(me()); }
		}
	}
	private SocketBay me() { return this; }
	
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