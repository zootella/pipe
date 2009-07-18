package pipe.user;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.OceanTheme;

public class MyTheme extends OceanTheme {
	
	protected ColorUIResource getBlack()                 { return new ColorUIResource(Skin.ink()); } // text color for lables, text boxes, and menu items
	public ColorUIResource getControlTextColor()         { return new ColorUIResource(Skin.ink()); } // text color for buttons
	public ColorUIResource getDesktopColor()             { return new ColorUIResource(Skin.page()); } // not seen
	public ColorUIResource getInactiveControlTextColor() { return new ColorUIResource(Skin.ghost()); } // ghosted button border and text
	public ColorUIResource getMenuDisabledForeground()   { return new ColorUIResource(Skin.ghost()); } // ghosted menu item text
	protected ColorUIResource getPrimary1() { return new ColorUIResource(Skin.border()); } // menu outline
	protected ColorUIResource getPrimary2() { return new ColorUIResource(Skin.page()); } // menu selection color and box around button that has keyboard focus
	protected ColorUIResource getPrimary3() { return new ColorUIResource(Skin.glow()); } // button highlight and text selection color
	protected ColorUIResource getSecondary1() { return new ColorUIResource(Skin.border()); } // button outline
	protected ColorUIResource getSecondary2() { return new ColorUIResource(Skin.page()); } // button shadow
	protected ColorUIResource getSecondary3() { return new ColorUIResource(Skin.page()); } // menu and dialog background
}
