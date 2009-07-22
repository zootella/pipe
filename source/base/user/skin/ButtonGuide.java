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

	public Rectangle ghost()  { return shift(0); }
	public Rectangle normal() { return shift(1); }
	public Rectangle light()  { return shift(2); }
	public Rectangle press()  { return shift(3); }

	private Rectangle shift(int i) {
		return new Rectangle(r.x, r.y + (i * r.height), r.width, r.height);
	}
}
