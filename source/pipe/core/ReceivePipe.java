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

	@Override public Path folder(Path p) {
		this.folder = p;
		return folder;
	}
	private Path folder;
	@Override public Path folder() {
		return folder;
	}

	@Override public Outline away(Outline away) {
		this.away = away;
		return away;
	}
	private Outline away;
	@Override public Outline away() {
		return away;
	}

	@Override public Outline home() {
		return new Outline();
	}
	
	@Override public String title() { return "Receive Pipe"; }
	@Override public String instruction() { return "Choose an empty folder to receive the incoming:"; }

	@Override public void go() {
	}


}
