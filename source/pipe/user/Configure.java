package pipe.user;

import javax.swing.UIManager;

import base.exception.PlatformException;

public class Configure {
	
	public static void user() {
		
		// Have Java Swing try to look native
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { throw new PlatformException(e); }

		//actually, you don't need this
//		System.setProperty("apple.laf.useScreenMenuBar", "true");
		
		
	}

}
