package pipe.core;

import javax.swing.JPanel;

import base.data.Outline;
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
	public boolean ready() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Path folder(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path folder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Outline away(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Outline away() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Outline home() {
		// TODO Auto-generated method stub
		return null;
	}

}
