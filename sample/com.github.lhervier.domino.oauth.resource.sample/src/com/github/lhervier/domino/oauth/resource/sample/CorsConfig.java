package com.github.lhervier.domino.oauth.resource.sample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Configuration
@EnableWebMvc
public class CorsConfig extends WebMvcConfigurerAdapter {

	/**
	 * Handle CORS requests by adding the response headers
	 * to OPTIONS responses
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(new HandlerInterceptorAdapter() {
			@Override
			public boolean preHandle(
					HttpServletRequest request,
					HttpServletResponse response, 
					Object handler) throws Exception {
				response.addHeader("Access-Control-Allow-Origin", "*");
				if( "OPTIONS".equals(request.getMethod()) ) {
					response.addHeader("Access-Control-Allow-Headers", "authorization");
			        response.addHeader("Access-Control-Max-Age", "60"); // seconds to cache preflight request --> less OPTIONS traffic
			        response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
			        response.setStatus(200);
				}
				return true;
			}
	    }).addPathPatterns("/apis/*");
	}
}
