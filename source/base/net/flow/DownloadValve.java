package base.net.flow;

import base.data.Bin;
import base.exception.ProgramException;
import base.net.socket.Socket;
import base.size.Meter;
import base.size.Range;
import base.state.Close;
import base.state.Update;
import base.valve.Valve;

public class DownloadValve extends Close implements Valve {
	
	// Make

	/** Make a DownloadValve that will download data from socket. */
	public DownloadValve(Update update, Socket socket, Range range) {
		this.update = update;
		this.socket = socket;
		meter = new Meter(range);
		out = Bin.medium();
	}
	
	/** The Update for the ValveList we're in. */
	private final Update update;
	/** The socket we download from. */
	private final Socket socket;
	/** Our current DownloadTask that downloads data from socket to out, null if we don't have one right now. */
	private DownloadTask task;

	/** Close this Valve so it gives up all resources and won't start again. */
	public void close() {
		if (already()) return;
		close(task);
		task = null; // Discard the closed later so in() and out() work
	}
	
	// Use
	
	public Bin in() { return null; }
	
	public Bin out() {
		if (is(task)) return null; // later's worker thread is using our bin, keep it private
		return out;
	}
	private Bin out;
	
	public Meter meter() { return meter; }
	private final Meter meter;
	
	public void start() {
		if (closed()) return;
		if (!meter.isDone() && no(task) && out.hasSpace())
			task = new DownloadTask(update, socket, meter.remain(), out);
	}
	
	public void stop() throws ProgramException {
		if (closed()) return;
		if (done(task)) {
			meter.add(task.result().size); // If an exception closed later, throw it
			task = null; // Discard the closed later, now in() and out() will work
		}
		if (meter.isDone()) close(this); // All done
	}
	
	public boolean isEmpty() {
		return
			no(task)     && // No later using our bins
			out.isEmpty() && // No data
			meter.isEmpty(); // No responsibility to do more
	}
}
