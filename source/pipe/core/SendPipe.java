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
	
	private Path folder;

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
