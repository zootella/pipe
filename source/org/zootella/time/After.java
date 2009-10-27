package org.zootella.time;

// tells you if you've waited long enough since something has happened, once it's happened
public class After {

	/** Make an After that will tell if it's been more than wait milliseconds since after.set(). */
	public After(long wait) {
		this.wait = wait;
	}
	
	private long set;
	private long wait;
	
	public void set() {
		set = Time.now();
	}
	
	public boolean enough() {
		if (set == 0)
			return false;
		
		return set + wait < Time.now(); 
	}

}
