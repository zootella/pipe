package example;

import javax.swing.JFrame;

public class ExampleFrame {

	public ExampleFrame() {

		frame = new JFrame("Example Frame");
		frame.setResizable(false);
		frame.setLayout(null);
		
		ExamplePanel grip = new ExamplePanel(frame);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(grip.panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	public final JFrame frame;
}
