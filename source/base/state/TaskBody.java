package base.state;

import base.exception.ProgramException;

/** Put your code for a separate thread to run in a TaskBody. */
public interface TaskBody {
	
	/**
	 * Your task object's separate thread will call this thread() method.
	 * Be careful your code only looks at final immutable objects here.
	 */
	public void thread() throws Exception;

	/**
	 * After thread() returns, the normal event thread will call this done() method.
	 * @return exception The exception your code in thread() threw, null if none
	 */
	public void done(ProgramException exception);
}
