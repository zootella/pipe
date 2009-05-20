package base.exception;

/**
 * Throw a PeerException when a remote peer you're communicating with says something not allowed.
 * When you catch a PeerException, disconnect from the remote peer.
 */
public class PeerException extends RuntimeException {}
