package pipe.core;

import javax.swing.JPanel;

import pipe.user.ExchangeDialog;
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
	
	public final Path folder;

	@Override public void close() {
		if (already()) return;
		
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
}
