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
import javax.swing.JPanel;

import base.process.Mistake;

public class GripPanel extends JPanel implements MouseMotionListener {
	
	public GripPanel(JFrame frame) {
		super(new GridBagLayout());
		
		this.frame = frame;
		GridBagLayout gridbag = (GridBagLayout) getLayout();
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.weighty = 1.0;

		c.insets = new Insets(1, 1, 1, 1);
		blankArea = new BlankArea(new Color(0.98f, 0.97f, 0.85f));
		gridbag.setConstraints(blankArea, c);
		add(blankArea);

		c.insets = new Insets(0, 0, 0, 0);

		//Register for mouse events on blankArea and panel.
		blankArea.addMouseMotionListener(this);
		addMouseMotionListener(this);

		setPreferredSize(new Dimension(450, 450));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		mouse = new MyMouseListener();
		blankArea.addMouseListener(mouse);
	}
	
	private final JFrame frame;
	private BlankArea blankArea;
	

	

	public void mouseMoved(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		
		System.out.println("dragged " + e.getX() + " " + e.getY());
	}

	
	
	
	
	
	private final MouseListener mouse;
    private class MyMouseListener extends MouseAdapter {
    	@Override public void mousePressed(MouseEvent m) {
    		try {
    			
    			System.out.println("pressed " + m.getX() + " " + m.getY());
    			
    		} catch (Exception e) { Mistake.stop(e); }
    	}
    }
	
	
	
	
	
	

}
