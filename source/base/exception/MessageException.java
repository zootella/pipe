package base.exception;

/**
 * Throw a MessageException when you're parsing a message and come to a mistake.
 * When you catch a MessageException, skip that message and try parsing the next one.
 */
public class MessageException extends RuntimeException {}
