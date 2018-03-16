package com.github.lhervier.domino.oauth.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

public class BaseOauthResourceConfig {

	@Bean
	public AccessTokenService createAccessTokenService() {
		return new AccessTokenService();
	}
	
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public BearerContext createBearerContext() {
		return new BearerContext();
	}
}
