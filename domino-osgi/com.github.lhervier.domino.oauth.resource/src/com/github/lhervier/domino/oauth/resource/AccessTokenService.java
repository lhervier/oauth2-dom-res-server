package com.github.lhervier.domino.oauth.resource;

import java.util.concurrent.TimeUnit;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Service to introspect an access token
 * @author Lionel HERVIER
 */
public class AccessTokenService {

	/**
	 * The access token introspection url
	 */
	@Value("${oauth2.resource.checkTokenUrl}")
	private String userInfo;
	
	/**
	 * The client Id
	 */
	@Value("${oauth2.resource.clientId}")
	private String clientId;
	
	/**
	 * The client secret
	 */
	@Value("${oauth2.resource.secret}")
	private String secret;
	
	/**
	 * An expiring map that will cache tokens
	 */
	private ExpiringMap<String, AccessToken> map = ExpiringMap
			.builder()
			.variableExpiration()
			.build();
	
	/**
	 * The rest template
	 */
	private RestTemplate template = new RestTemplate();
	
	/**
	 * return a parsed token
	 */
	public AccessToken getIdToken(String accessToken) throws InvalidTokenException {
		AccessToken ret = this.map.get(accessToken);
		if( ret != null )
			return ret;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String auth = String.format("%s:%s", this.clientId, this.secret);
		headers.add("Authorization", "Basic " + Utils.b64Encode(auth));
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("token", accessToken);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		try {
			ResponseEntity<AccessToken> resp = this.template.postForEntity(this.userInfo, request, AccessToken.class);
			if( resp.getStatusCode() != HttpStatus.OK )
				throw new InvalidTokenException("Introspection endpoint returns http status code : '" + resp.getStatusCode().name() + "'");
			ret = resp.getBody();
			if( !resp.getBody().isActive() )
				throw new InvalidTokenException("Token is invalid");
			
			long expiration = System.currentTimeMillis() / 1000L;
			expiration = ret.getExp() - expiration;
			if( expiration < 0 )
				throw new InvalidTokenException("Token expired");
			
			this.map.put(accessToken, ret, ExpirationPolicy.CREATED, expiration, TimeUnit.SECONDS);
			return ret;
		} catch(HttpClientErrorException e) {
			if( e.getStatusCode() == HttpStatus.UNAUTHORIZED )
				throw new RuntimeException("Invalid clientId and/or secret in configuration");
			throw new RuntimeException(e);
		}
	}
}
