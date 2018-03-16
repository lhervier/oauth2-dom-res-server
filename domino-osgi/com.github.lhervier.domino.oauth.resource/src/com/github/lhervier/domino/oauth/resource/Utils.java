package com.github.lhervier.domino.oauth.resource;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import lotus.domino.Base;
import lotus.domino.NotesException;

public class Utils {

	/**
	 * Encode a string to base64
	 * @param s the string to encode
	 * @return the encoded string
	 */
	public static final String b64Encode(String s) {
		try {
			return new String(Base64.encodeBase64(s.getBytes("UTF-8")), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Recycle a Domino object
	 * @param o the object
	 */
	public static final void recycleQuietly(Base o) {
		if( o == null )
			return;
		try {
			o.recycle();
		} catch(NotesException e) {
		}
	}
	
	/**
	 * To know if a string is empty
	 */
	public static final boolean isEmpty(String s) {
		if( s == null )
			return true;
		return s.length() == 0;
	}
}
