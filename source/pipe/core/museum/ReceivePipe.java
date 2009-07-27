package pipe.core.museum;

import pipe.user.PipeInfoFrame;
import pipe.user.PipePanel;
import base.data.Outline;
import base.file.Path;
import base.state.Close;

public class ReceivePipe extends Close implements Pipe {

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PipePanel panel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean ready() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean folder(Path p) {
		this.folder = p;
		return false;
	}
	private Path folder;

	@Override public boolean away(String away) {
//		this.away = away;
		return false;
	}
	private Outline away;

	@Override public String home() {
		return "yeah";
	}
	
	@Override public String title() { return "Receive Pipe"; }
	@Override public String instruction() { return "Choose an empty folder to receive the incoming:"; }

	@Override public void go() {
	}

	@Override
	public PipeInfoFrame info() {
		// TODO Auto-generated method stub
		return null;
	}


}
