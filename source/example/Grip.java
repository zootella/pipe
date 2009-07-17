package example;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import base.process.Mistake;

public class Grip {
	
	public Grip(JFrame frame, Rectangle rectangle) {
		this.frame = frame;
		
		label = new JLabel();
		label.setBounds(rectangle);
	    label.setBackground(new Color(0xfee5ac));
	    label.setOpaque(true);
	    
	    mouse = new MyMouseListener();
	    motion = new MyMouseMotionListener();
	    
	    label.addMouseListener(mouse);
	    label.addMouseMotionListener(motion);
		
		
		
	}
	
	public final JFrame frame;
	public final JLabel label;
	
	
	
	private Point a;
	private Point b;
	
	
	
	
	private final MouseListener mouse;
    private class MyMouseListener extends MouseAdapter {
    	@Override public void mousePressed(MouseEvent m) {
    		try {
    			
    			a = m.getPoint();
    			
    		} catch (Exception e) { Mistake.stop(e); }
    	}
    }

	private final MouseMotionListener motion;
	private class MyMouseMotionListener implements MouseMotionListener {
		@Override public void mouseMoved(MouseEvent m) {}
		@Override public void mouseDragged(MouseEvent m) {
			try {
				
				b = m.getPoint();
				
				Point f = frame.getLocation();
				
				Point g = new Point(f.x + b.x - a.x, f.y + b.y - a.y);
				
				System.out.println(a.toString() + " to " + b.toString() + " move " + g.toString());
				
				frame.setLocation(g);
				
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}
	
	
	
	
	
	
	

}
