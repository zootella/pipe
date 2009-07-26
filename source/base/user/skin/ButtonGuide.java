package base.user.skin;

import java.awt.Rectangle;

public class ButtonGuide {
	
	public ButtonGuide(int x, int y, int w, int h) {
		this(new Rectangle(x, y, w, h));
	}
	
	public ButtonGuide(Rectangle r) {
		this.r = r;
	}
	private final Rectangle r;

	public static final int ghost  = 0;
	public static final int normal = 1;
	public static final int hot    = 2;
	public static final int press  = 3;

	public Rectangle get(int state) { return shift(state); }

	private Rectangle shift(int i) {
		if (i < ghost || i > press) throw new IllegalArgumentException();
		return new Rectangle(r.x, r.y + (i * r.height), r.width, r.height);
	}
	
	//TODO maybe move this all into SkinButton?
}
