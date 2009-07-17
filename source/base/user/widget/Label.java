package base.user.widget;

import javax.swing.JLabel;

import base.user.Graphics;

public class Label {
	
	public Label(String s) {
		label = new JLabel(s);
		label.setFont(Graphics.font());
		label.setForeground(Graphics.foreground());
	}
	
	
	public JLabel label;

}
