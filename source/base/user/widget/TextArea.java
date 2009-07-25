package base.user.widget;

import javax.swing.JTextArea;

import base.user.Face;

public class TextArea {
	
	public TextArea() {
		area = new JTextArea();
		area.setFont(Face.font());
		new TextMenu(area);
	}
	
	public final JTextArea area;
}
