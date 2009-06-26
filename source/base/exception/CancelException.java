package base.exception;

/**
 * Throw a CancelException when the user presses Cancel to abandon setting something up.
 * When you catch a CancelException, remove ui from the screen and don't save the data you were preparing.
 */
public class CancelException extends RuntimeException {}