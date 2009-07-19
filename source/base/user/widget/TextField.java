package base.user.widget;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;

import pipe.user.Skin;

public class TextField {
	
	public TextField() {
		field = new JTextField();
//		field.setFont(Skin.font());
		
		/*
		field.setBorder(new LineBorder(Skin.border()));
		field.setCaretColor(Skin.ink());
		field.setDisabledTextColor(Skin.ghost());
		field.setForeground(Skin.ink());
		field.setSelectedTextColor(Skin.highInk());
		field.setSelectionColor(Skin.high());
		*/
		
		new TextMenu(field);
	}
	
	public final JTextField field;
}
