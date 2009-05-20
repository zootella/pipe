package base.exception;

/**
 * Throw a DiskException when a file or folder on the disk breaks.
 * When you catch a DiskException, give up the operation that needs the disk.
 */
public class DiskException extends RuntimeException {}
