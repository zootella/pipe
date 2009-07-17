package example;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ExampleFrame {

	public ExampleFrame() {

		frame = new JFrame("Example Frame");

		JComponent newContentPane = new GripPanel(frame);
		newContentPane.setOpaque(true); //content panes must be opaque

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(newContentPane);
		frame.pack();
		frame.setVisible(true);
	}
	
	public final JFrame frame;
}
