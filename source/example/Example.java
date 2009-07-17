package example;

public class Example {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				new ExampleFrame();
			}
		});
	}
}
