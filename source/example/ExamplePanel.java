package example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import base.process.Mistake;

public class ExamplePanel {
	
	public ExamplePanel(JFrame frame) {
		
		panel = new JPanel(new GridBagLayout());
		
		this.frame = frame;
		GridBagLayout gridbag = (GridBagLayout)panel.getLayout();
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.weighty = 1.0;

		c.insets = new Insets(1, 1, 1, 1);
		label = new JLabel();
	    label.setBackground(new Color(0.98f, 0.97f, 0.85f));
	    label.setOpaque(true);
		
		
		gridbag.setConstraints(label, c);
		panel.add(label);

		c.insets = new Insets(0, 0, 0, 0);

		motion = new MyMouseMotionListener();
		label.addMouseMotionListener(motion);

		panel.setPreferredSize(new Dimension(450, 450));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel.setOpaque(true); // example says this is necessary
		
		mouse = new MyMouseListener();
		label.addMouseListener(mouse);
	}
	
	private final JFrame frame;
	public final JPanel panel;
	private final JLabel label;
	
	
	private final MouseListener mouse;
    private class MyMouseListener extends MouseAdapter {
    	@Override public void mousePressed(MouseEvent m) {
    		try {
    			
    			System.out.println("pressed " + m.getX() + " " + m.getY() + " --");
    			
    		} catch (Exception e) { Mistake.stop(e); }
    	}
    }

	private final MouseMotionListener motion;
	private class MyMouseMotionListener implements MouseMotionListener {
		@Override public void mouseMoved(MouseEvent m) {}
		@Override public void mouseDragged(MouseEvent m) {
			try {
				
				System.out.println("dragged " + m.getX() + " " + m.getY());
				
			} catch (Exception e) { Mistake.stop(e); }
		}
	}
	
	
	
	
	
	
	
	
	

}
