package com.github.lhervier.domino.oauth.resource.sample.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.lhervier.domino.oauth.resource.InvalidTokenException;
import com.github.lhervier.domino.oauth.resource.NoTokenException;

/**
 * Handle exceptions
 * @author Lionel hervier
 */
@ControllerAdvice
public class ExceptionController {

	/**
	 * Logger
	 */
	private static final Log LOG = LogFactory.getLog(ExceptionController.class);
	
	/**
	 * Invalid token => 403
	 */
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<Void> processInvalidTokenException(InvalidTokenException e) {
		LOG.info("Forbidden : ", e);
		return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
	}
	
	/**
	 * No token => 403 
	 */
	@ExceptionHandler(NoTokenException.class)
	public ResponseEntity<Void> processNoTokenException(NoTokenException e) {
		LOG.info("Not Authorized : ", e);
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	/**
	 * Exception
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Void> processRuntimeException(Exception e) {
		LOG.fatal("", e);
		return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
