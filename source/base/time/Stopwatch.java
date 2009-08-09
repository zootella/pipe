package base.time;

public class Stopwatch {
	
	public Stopwatch() {
		start = new Now();
	}
	private final Now start;
	private Now stop;
	public void stop() {
		stop = new Now();
	}
	@Override public String toString() {
		if (stop == null) return "running";
		return (stop.time - start.time) + " milliseconds";
	}

}
