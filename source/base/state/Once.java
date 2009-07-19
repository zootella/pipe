package base.state;

/** A Once protects you from doing something more than once. */
public class Once {
	
	private boolean done;

	/** true the first time you call once(), false every time afterwards. */
	public boolean once() {
		if (done) return false;
		done = true;
		return true;
	}
}
