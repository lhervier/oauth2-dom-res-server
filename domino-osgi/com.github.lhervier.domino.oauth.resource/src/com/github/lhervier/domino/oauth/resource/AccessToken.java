package com.github.lhervier.domino.oauth.resource;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessToken {

	/**
	 * Active ?
	 */
	private boolean active;
	
	/**
	 * Expiration
	 */
	private long exp;
	
	/**
	 * The issuer
	 */
	private String iss;
	
	/**
	 * The audiance
	 */
	private String aud;
	
	/**
	 * The subject
	 */
	private String sub;
	
	/**
	 * The scope
	 */
	private String scope;

	public long getExp() { return exp; }
	public void setExp(long exp) { this.exp = exp; }
	public String getIss() { return iss; }
	public void setIss(String iss) { this.iss = iss; }
	public String getAud() { return aud; }
	public void setAud(String aud) { this.aud = aud; }
	public String getSub() { return sub; }
	public void setSub(String sub) { this.sub = sub; }
	public String getScope() { return scope; }
	public void setScope(String scope) { this.scope = scope; }
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
}
