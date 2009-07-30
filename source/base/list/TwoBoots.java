package base.list;

import java.util.HashSet;
import java.util.Set;

import base.state.Close;
import base.time.Now;

public class TwoBoots<T> extends Close {
	
	// Make

	/**
	 * Make a new TwoBoots<T>() to hold objects of type T.
	 * It will keep an object you add for at least delay milliseconds but not twice that long.
	 * If the objects inside extend Close, we'll call close() on them before throwing them out.
	 */
	public TwoBoots(long delay) {
		this.delay = delay;
		age = new Now();
		current = new HashSet<T>();
		previous = new HashSet<T>();
	}
	
	private final long delay; // Every delay milliseconds, we cycle the boots
	private Now age;          // When we last cycled the boots
	private Set<T> current;   // The current boot we add objects to
	private Set<T> previous;  // The previous boot we just keep around

	@Override public void close() {
		if (already()) return;
		closeContents(current);
		closeContents(previous);
	}
	
	// Keep

	/** Add t to this TwoBoots, we'll keep it for awhile, then close and discard it. */
	public void add(T t) {
		open();
		cycle();
		if (!current.contains(t) && !previous.contains(t))
			current.add(t);
	}

	/** Remove t from this TwoBoots, does not close it. */
	public void remove(T t) {
		open();
		cycle();
		current.remove(t);
		previous.remove(t);
	}

	/** All the objects currently in this TwoBoots. */
	public Set<T> list() {
		open();
		cycle();
		Set<T> set = new HashSet<T>();
		set.addAll(current);
		set.addAll(previous);
		return set;
	}
	
	// Help

	/** If it's been long enough since the last time, close and remove the oldest objects we carry. */
	private void cycle() {
		if (age.expired(delay)) {
			age = new Now();
			closeContents(previous);
			previous = current;
			current = new HashSet<T>();
		}
	}
	
	/** If we're holding objects that extend Close, call close() on all of them in set. */
	private void closeContents(Set<T> set) {
		for (T t : set)
			if (t instanceof Close)
				close((Close)t);
	}
}
