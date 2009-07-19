package base.user.skin;

import javax.swing.JLabel;

import base.user.Fonts;

public class SkinLabel {
	
	public SkinLabel() {
		label = new JLabel();
		label.setFont(Fonts.program());
	}
	
	public final JLabel label;
}
