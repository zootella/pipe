package pipe.core;

import javax.swing.JPanel;

import pipe.user.ExchangeDialog;
import base.data.Outline;
import base.file.Path;
import base.state.Close;

public class SendPipe extends Close implements Pipe {
	
	public SendPipe() {
		
		
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
	

	@Override public void close() {
		if (already()) return;
		
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

	@Override public String title() { return "Send Pipe"; }
	@Override public String instruction() { return "Choose the folder you want to send:"; }


	@Override public void go() {
	}

}
