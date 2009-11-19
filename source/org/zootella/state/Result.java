package org.zootella.state;

import org.zootella.exception.ProgramException;
import org.zootella.time.Duration;

public class Result<T> {
	
	public Result(T t, Duration duration) {
		this.result = t;
		this.duration = duration;
		this.exception = null;
	}
	
	public Result(T t, Duration duration, ProgramException exception) {
		this.result = t;
		this.duration = duration;
		this.exception = exception;
	}
	
	public final T result;
	public final Duration duration;
	public final ProgramException exception;
}
