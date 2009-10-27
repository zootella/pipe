package org.zootella.user.widget;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.zootella.process.Mistake;

public class Grip {
	
	public Grip(JFrame frame, JComponent panel) {
		this.frame = frame;
	    
		panel.addMouseListener(new MyMouseListener());
		panel.addMouseMotionListener(new MyMouseMotionListener());
	}
	
	private final JFrame frame;

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
