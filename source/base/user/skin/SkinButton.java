package base.user.skin;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JLabel;

import pipe.user.Guide;
import base.process.Mistake;
import base.user.Face;

public class SkinButton {
	
	public SkinButton(Action action, Skin skin, ButtonGuide guide, Rectangle place) {
		this.action = action;
		this.skin = skin;
		this.guide = guide;

		label = new MyLabel();
		label.setLayout(null);
		label.setBounds(place);
		label.setFont(Face.skin());
		label.setForeground(skin.color(Guide.skinInk));
	    label.addMouseListener(new MyMouseListener());
	    
	    action.addPropertyChangeListener(new MyPropertyChangeListener());
	    
	    state = ButtonGuide.normal;
	}
	
	private final Action action;
	private final Skin skin;
	private final ButtonGuide guide;
	public final MyLabel label;

	private class MyLabel extends JLabel {
		@Override public void paintComponent(Graphics g) {
			g.drawImage(skin.image(guide.get(state)), 0, 0, null);
			g.drawString("hello", 0, 0);
		}
	}

	private int state;

    private class MyMouseListener extends MouseAdapter {
    	@Override public void mouseEntered(MouseEvent m) {
    		try {
    			state = ButtonGuide.hot;
    			label.repaint();
    		} catch (Exception e) { Mistake.stop(e); }
    	}
    	@Override public void mouseExited(MouseEvent m) {
    		try {
    			state = ButtonGuide.normal;
    			label.repaint();
    		} catch (Exception e) { Mistake.stop(e); }
    	}
    	@Override public void mousePressed(MouseEvent m) {
    		try {
    			state = ButtonGuide.press;
    			label.repaint();
    		} catch (Exception e) { Mistake.stop(e); }
    	}
    	@Override public void mouseClicked(MouseEvent m) {
    		try {
    			action.actionPerformed(null);
    		} catch (Exception e) { Mistake.stop(e); }
    	}
    }
    
    private class MyPropertyChangeListener implements PropertyChangeListener {
    	@Override public void propertyChange(PropertyChangeEvent p) {
    		
    		//TODO somewhere in here you can tell if the action is enabled or not
		}
    }
    
    
    
    // Shift
	
	public static final int ghost  = 0;
	public static final int normal = 1;
	public static final int hot    = 2;
	public static final int press  = 3;

	private static Rectangle shift(Rectangle r, int state) {
		return new Rectangle(r.x, r.y + (state * r.height), r.width, r.height);
	}
	
	
	
	
	
}
