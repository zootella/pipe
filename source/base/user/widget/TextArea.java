package base.user.widget;

import javax.swing.JTextArea;

import base.user.Fonts;

public class TextArea {
	
	public TextArea() {
		area = new JTextArea();
		area.setFont(Fonts.platform());
		new TextMenu(area);
	}
	
	public final JTextArea area;
}
