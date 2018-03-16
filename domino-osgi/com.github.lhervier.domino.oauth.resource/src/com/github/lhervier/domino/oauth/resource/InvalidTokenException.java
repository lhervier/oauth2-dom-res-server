package com.github.lhervier.domino.oauth.resource;

/**
 * Exception raised when the bearer token is invalid
 * @author Lionel HERVIER
 */
public class InvalidTokenException extends Exception {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 4085704470411036921L;

	/**
	 * Constructor
	 * @param msg the message
	 * @param cause the cause
	 */
	public InvalidTokenException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructor
	 * @param msg the message
	 */
	public InvalidTokenException(String msg) {
		super(msg);
	}
}
