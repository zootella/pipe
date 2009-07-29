package base.data;

import java.util.HashSet;
import java.util.Set;

import base.state.Close;
import base.time.Now;

public class TwoBoots {

	/** Make a TwoBoots that will hold Close objects for at least delay milliseconds but not twice that long. */
	public TwoBoots(long delay) {
		this.delay = delay;
		current = new HashSet<Close>();
		previous = new HashSet<Close>();
		age = new Now();
	}
	
	/** The current boot we add new Close objects to. */
	private Set<Close> current;
	/** The previous boot from before. */
	private Set<Close> previous;

	/** The number of milliseconds between times we cycle the boots. */
	private final long delay;
	/** When we last cycled the boots. */
	private Now age;

	/** Add c to this TwoBoots, we'll keep it for awhile, then close and discard it. */
	public void add(Close c) {
		cycle();
		if (!current.contains(c) && !previous.contains(c))
			current.add(c);
	}

	/** Remove c from this TwoBoots if we still have it, close it either way. */
	public void remove(Close c) {
		cycle();
		Close.close(c);
		current.remove(c);
		previous.remove(c);
	}

	/** Get all the Close objects currently in this TwoBoots. */
	public Set<Close> get() {
		cycle();
		Set<Close> s = new HashSet<Close>();
		s.addAll(current);
		s.addAll(previous);
		return s;
	}

	/** If it's been long enough since the last time, close and remove the oldest objects we carry. */
	private void cycle() {
		if (age.expired(delay)) {
			age = new Now();
			for (Close c : previous)
				Close.close(c);
			previous = current;
			current = new HashSet<Close>();
		}
	}
}
