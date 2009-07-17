package base.user;

import java.awt.Color;
import java.awt.Font;

public class Graphics {

	public static Font font() {
		if (font == null)
			font = new Font("Tahoma", Font.PLAIN, 11);
		return font;
	}
	private static Font font;
	
	public static Color background() {
		return new Color(0xffffff);
	}
	
	public static Color foreground() {
		return new Color(0x1589FF);
	}

}
