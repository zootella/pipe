package example;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

public class BlankArea extends JLabel {
	  Dimension minSize = new Dimension(100, 100);

	  public BlankArea(Color color) {
	    setBackground(color);
	    setOpaque(true);
//	    setBorder(BorderFactory.createLineBorder(Color.black));
	  }

	  public Dimension getMinimumSize() {
	    return minSize;
	  }

	  public Dimension getPreferredSize() {
	    return minSize;
	  }
	}
