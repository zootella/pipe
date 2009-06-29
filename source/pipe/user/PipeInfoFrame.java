package pipe.user;

import pipe.core.Pipe;
import pipe.main.Program;

public class PipeInfoFrame {
	
	public PipeInfoFrame(Program program, Pipe pipe) {
		this.program = program;
		this.pipe = pipe;
	}
	
	private final Program program;
	private final Pipe pipe;

}
