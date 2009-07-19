package base.user.widget;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import base.process.Mistake;

public class Grip {
	
	public Grip(JFrame frame, Rectangle rectangle) {
		this.frame = frame;
		
		label = new JLabel();
		label.setBounds(rectangle);
	    label.setBackground(new Color(0xdedede)); //TODO remove this later
	    label.setOpaque(true);
	    
	    label.addMouseListener(new MyMouseListener());
	    label.addMouseMotionListener(new MyMouseMotionListener());
	}
	
	private final JFrame frame;
	public final JLabel label;
	private Point press;

    private class MyMouseListener extends MouseAdapter {
    	@Override public void mousePressed(MouseEvent m) {
    		try {

    			// Remember where the drag started
    			press = m.getPoint();
    			
    		} catch (Exception e) { Mistake.stop(e); }
    	}
    }

	private class MyMouseMotionListener implements MouseMotionListener {
		@Override public void mouseMoved(MouseEvent m) {}
		@Override public void mouseDragged(MouseEvent m) {
			try {

				// Nudge the frame the distance and direction of the drag
				if (press != null)
					frame.setLocation(
						frame.getLocation().x + m.getPoint().x - press.x,
						frame.getLocation().y + m.getPoint().y - press.y);

			} catch (Exception e) { Mistake.stop(e); }
		}
	}
}
