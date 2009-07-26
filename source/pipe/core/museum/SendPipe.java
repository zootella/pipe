package pipe.core.museum;

import pipe.main.Program;
import pipe.user.PipeInfoFrame;
import pipe.user.PipePanel;
import base.data.Outline;
import base.file.Path;
import base.state.Close;

public class SendPipe extends Close implements Pipe {

	@Override public String title() { return "Send Pipe"; }
	@Override public String instruction() { return "Choose the folder you want to send:"; }
	
	public SendPipe(Program program) {
		this.program = program;
		panel = new PipePanel(program, this);
		info = new PipeInfoFrame(program, this);
	}
	
	private final Program program;

	@Override public void close() {
		if (already()) return;
		
		close(info);
	}
	
	@Override public PipePanel panel() { return panel; }
	private final PipePanel panel;
	
	@Override public PipeInfoFrame info() { return info; }
	private final PipeInfoFrame info;

	@Override public boolean ready() {
		return false;
	}

	@Override public Path folder(Path p) {
		this.folder = p;
		return folder;
	}
	@Override public Path folder() { return folder; }
	private Path folder;

	@Override public Outline away(Outline away) {
		this.away = away;
		return away;
	}
	@Override public Outline away() { return away; }
	private Outline away;

	@Override public Outline home() {
		return new Outline();
	}


	@Override public void go() {
	}

}
