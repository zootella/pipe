package base.time;

public class Spacer {
	
	public Spacer(int space) {
		if (space < 0) throw new IllegalArgumentException();
		this.space = space;
		
	}
	
	private final int space;
	
	private Now now;

}
