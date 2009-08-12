package base.encrypt.secret;

import javax.crypto.Cipher;

import base.data.Bin;
import base.size.Meter;
import base.state.Close;
import base.state.Update;
import base.valve.Valve;

public class SecretValve extends Close implements Valve {

	public SecretValve(Update update, Cipher cipher, int mode) {
		this.update = update;
		this.cipher = cipher;
		this.mode = mode;
		in = Bin.medium();
		out = Bin.medium();
		meter = new Meter();
	}
	
	private final Update update;
	private final Cipher cipher;
	private final int mode;
	private final Bin in;
	private final Bin out;
	private final Meter meter;
	private SecretTask task;

	@Override public void close() {
		if (already()) return;
		close(task);
		task = null; // Discard the closed task so in() and out() will work
	}

	public Bin in() { if (is(task)) return null; return in; }
	public Bin out() { if (is(task)) return null; return out; }
	public Meter meter() { return meter; }
	
	public void start() {
		if (closed()) return;
		if (!meter.isDone() && no(task) && Secret.can(cipher, mode, in, out))
			task = new SecretTask(update, cipher, mode, in, out);
	}

	public void stop() throws Exception {
		if (closed()) return;
		if (done(task)) {
			meter.add(task.result().size); // If an exception closed task, result() will throw it
			task = null; // Discard the closed task so in() and out() will work
		}
		if (meter.isDone()) close(this); // All done
	}
	
	public boolean isEmpty() {
		return
			no(task)     &&  // No task using our bins
			in.isEmpty() &&  // No data
			meter.isEmpty(); // No responsibility to do more
	}
}
