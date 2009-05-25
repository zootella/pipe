package pipe.core;

import javax.swing.JPanel;

import base.file.Path;
import base.state.Close;

public class ReceivePipe extends Close implements Pipe {

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel panel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean readyToStart() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Path folder(String s) {
		// TODO Auto-generated method stub
		return null;
	}

}
