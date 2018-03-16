package com.github.lhervier.domino.oauth.resource;

/**
 * Exception raised if no access token is provided
 * @author Lionel HERVIER
 */
public class NoTokenException extends Exception {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -6443915570573422132L;

	/**
	 * Constructor
	 * @param msg the message
	 * @param cause the cause
	 */
	public NoTokenException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructor
	 * @param msg the message
	 */
	public NoTokenException(String msg) {
		super(msg);
	}

}
