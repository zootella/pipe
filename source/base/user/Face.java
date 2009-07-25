package base.user;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.UIManager;

import base.exception.PlatformException;

public class Face {
	
	// Appearance
	
	/** Tell Java Swing to try to look native. */
	public static void blend() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { throw new PlatformException(e); }
	}

	// Font

	/** The default Font the current look and feel is using. */
	public static Font font() {
		if (platform == null) {
			JLabel label = new JLabel("");
			platform = label.getFont();
		}
		return platform;
	}
	private static Font platform;
	
	/** A cross-platform Font that should look good and the same everywhere. */
	public static Font skin() {
		if (program == null)
			program = new Font("Tahoma", Font.PLAIN, 11);
		return program;
	}
	private static Font program;
	
	// Image

	public static Image image(String path) {
		return Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(path));
	}
}
