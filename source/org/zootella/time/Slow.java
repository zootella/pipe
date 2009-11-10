package org.zootella.time;

public class Slow {
	
	public Slow(Now now) {
		duration = new Duration(now);
		
		System.out.println(duration.toString());
		/*
		if (duration.time() > Time.second / 10)
			throw new IllegalStateException(duration.toString());
			*/
	}
	
	public final Duration duration;

}
