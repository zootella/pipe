package pipe.user;

import java.awt.Color;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import pipe.main.Program;
import base.exception.PlatformException;
import base.state.Close;

public class User extends Close {

	// Object
	
	public User(Program program) {
		
		

		
		/*
		MetalLookAndFeel.setCurrentTheme(new MyTheme());
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) { throw new PlatformException(e); }
		
		
		UIDefaults defaults = UIManager.getDefaults();
		 */
		

		//necessary
		/*
		defaults.put("MenuItem.selectionBackground", Skin.high()); // selection color
		defaults.put("MenuItem.selectionForeground", Skin.highInk()); // selected text color
		*/
		
		//unnecessary
		/*
		defaults.put("MenuItem.background", Skin.page()); //background color
		defaults.put("MenuItem.disabledForeground", Skin.ghost()); // grayed out text color
		defaults.put("MenuItem.foreground", Skin.ink()); // text color
		defaults.put("MenuItem.acceleratorForeground", Color.red); //dn
		defaults.put("MenuItem.acceleratorSelectionForeground", Color.red); //dn
		defaults.put("PopupMenu.border", new LineBorder(Color.red));
		defaults.put("MenuItem.border", new LineBorder(Color.red));//disaster
		defaults.put("Menu.border", new LineBorder(Color.red));//nothing
		defaults.put("InternalFrame.border", new LineBorder(Color.red));//nothing
		 */

		
		/*
		BorderUIResource border = (BorderUIResource)defaults.get("PopupMenu.border");
		BorderUIResource.getBlackLineBorderUIResource();
		
		Border b;
		b = new Border();
		
		
		/*
		 */

		//can't you do this in the item itself?
		
		
		
		
		
		
		
		
		

		main = new MainFrame(program);
		info = new InfoFrame(program);
		icon = new MainIcon(program);

		show(true);
	}
	
	public final MainFrame main;
	public final InfoFrame info;
	public final MainIcon icon;

	private boolean show;
	public void show(boolean b) {
		if (show == b) return;
		show = b;

		main.frame.setVisible(show);
		icon.show(!show);
	}

	@Override public void close() {
		if (already()) return;
		
		close(main);
		close(info);
		close(icon);
	}
}
