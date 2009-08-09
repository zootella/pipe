package base.net.flow;

import base.data.Bin;
import base.exception.ProgramException;
import base.net.socket.Socket;
import base.size.Meter;
import base.size.Range;
import base.state.Close;
import base.state.Update;
import base.valve.Valve;

public class UploadValve extends Close implements Valve {
	
	// Make

	/** Make an UploadValve that will upload data into socket. */
	public UploadValve(Update update, Socket socket, Range range) {
		this.update = update;
		this.socket = socket;
		meter = new Meter(range);
		in = Bin.medium();
	}
	
	/** The Update for the ValveList we're in. */
	private final Update update;
	/** The socket we upload to. */
	private final Socket socket;
	/** Our current UploadTask that uploads data from in to socket, null if we don't have one right now. */
	private UploadTask task;

	/** Close this Valve so it gives up all resources and won't start again. */
	public void close() {
		if (already()) return;
		close(task);
		task = null; // Discard the closed later so in() and out() work
	}
	
	// Use

	public Bin in() {
		if (is(task)) return null; // later's worker thread is using our bin, keep it private
		return in;
	}
	private Bin in;
	
	public Bin out() { return null; }
	
	public Meter meter() { return meter; }
	private final Meter meter;

	public void start() {
		if (closed()) return;
		if (!meter.isDone() && no(task) && in.hasData())
			task = new UploadTask(update, socket, meter.remain(), in);
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
			no(task)      && // No later using our bins
			in.isEmpty()  && // No data
			meter.isEmpty(); // No responsibility to do more
	}
}
