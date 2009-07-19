package base.user;

import java.awt.Font;

import javax.swing.JLabel;

public class Fonts {

	/** The default Font the current look and feel is using. */
	public static Font platform() {
		if (platform == null) {
			JLabel label = new JLabel("");
			platform = label.getFont();
		}
		return platform;
	}
	private static Font platform;
	
	/** A cross-platform Font that should look good and the same everywhere. */
	public static Font program() {
		if (program == null)
			program = new Font("Tahoma", Font.PLAIN, 11);
		return program;
	}
	private static Font program;
}
