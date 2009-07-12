package base.file;


import base.data.Bin;
import base.size.Meter;
import base.size.Range;
import base.state.Close;
import base.state.Update;
import base.valve.Valve;

public class WriteValve extends Close implements Valve {
	
	// Make
	
	/** Make a WriteValve that will take data from in() and write it at index in file. */
	public WriteValve(Update update, File file, Range range) {
		this.update = update;
		this.file = file;
		meter = new Meter(range);
		in = Bin.medium();
	}
	
	/** The Update for the ValveList we're in. */
	private final Update update;
	/** The open File we write to. */
	private final File file;
	/** Our current WriteLater, null if we don't have one right now. */
	private WriteTask later;

	/** Close this Valve so it gives up all resources and won't start again. */
	public void close() {
		if (already()) return;
		if (later != null) {
			close(later);
			later = null; // Discard the closed later so in() and out() work
		}
	}
	
	// Use

	public Bin in() {
		if (later != null) return null; // later's worker thread is using our bin, keep it private
		return in;
	}
	private Bin in;
	
	public Bin out() { return null; }
	
	public Meter meter() { return meter; }
	private final Meter meter;
	
	public void start() {
		if (closed()) return;
		if (!meter.isDone() && later == null && in.hasData())
			later = new WriteTask(update, file, meter.remain(), in);
	}
	
	public void stop() throws Exception {
		if (closed()) return;
		if (later != null && later.closed()) { // Our later finished
			meter.add(later.result().stripe.size); // If an exception closed later, throw it
			later = null; // Discard the closed later, now in() and out() will work
		}
		if (meter.isDone()) close(this); // All done
	}
	
	public boolean isEmpty() {
		return
			later == null && // No later using our bins
			in.isEmpty()  && // No data
			meter.isEmpty(); // No responsibility to do more
	}
}
