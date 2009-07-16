package base.exception;

public class MyException extends RuntimeException {

	public MyException() {}
	
	public MyException(String message) {
		super(message);
	}
	
	RuntimeException() 
    Constructs a new runtime exception with null as its detail message.
RuntimeException(String message) 
    Constructs a new runtime exception with the specified detail message.
RuntimeException(String message, Throwable cause) 
    Constructs a new runtime exception with the specified detail message and cause.
RuntimeException(Throwable cause) 
    Constructs a new runtime exception with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
}
