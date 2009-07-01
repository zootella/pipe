package pipe.core;

import pipe.main.Program;
import pipe.user.PipeInfoFrame;
import pipe.user.PipePanel;
import base.data.Outline;
import base.file.Path;
import base.state.Close;

public class SendPipe extends Close implements Pipe {
	
	public SendPipe(Program program) {
		this.program = program;
		panel = new PipePanel(program, this);
		info = new PipeInfoFrame(program, this);
		
		
		/*
		new ExchangeDialog(program, pipe);
		
		if (pipe.readyToStart()) {
			
			program.core.pipes.add(pipe);
			program.core.changed();
			
		} else {
			
			pipe.close();
		}
		*/
		
		
		
		
		
		
		
		
	}
	
	private final Program program;
	

	@Override public void close() {
		if (already()) return;
		
		close(info);
	}
	
	private final PipePanel panel;
	@Override public PipePanel panel() { return panel; }
	
	private final PipeInfoFrame info;
	@Override public PipeInfoFrame info() { return info; }

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

	@Override public String title() { return "Send Pipe"; }
	@Override public String instruction() { return "Choose the folder you want to send:"; }


	@Override public void go() {
	}

}
