package pipe.user;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;

import javax.swing.JLabel;

public class Skin {

	public static Font font() {
		if (font == null) {
			JLabel label = new JLabel("hai");
			font = label.getFont();
		}
		return font;
	}
	private static Font font;
	
	
	//skinnable colors
	/*
	public static Color page()    { return new Color(0xffffff); }
	public static Color border()  { return new Color(0x999999); }//choose
	public static Color ink()     { return new Color(0x333333); }//choose
	public static Color ghost()   { return new Color(0xcccccc); }//choose
	public static Color high()    { return new Color(0x3399ff); }//choose
	public static Color highInk() { return new Color(0xffffff); }//choose
	public static Color glow()    { return new Color(0xffcc00); }//from chrome f1ca80, getbot ffcc00
	/*
	*/
	
	/*
	 * normalPage
	 * normalInk
	 * 
	 * ghostInk
	 * 
	 * highPage
	 * highInk
	 * 
	 * 
	 * 
	 */
	

}
