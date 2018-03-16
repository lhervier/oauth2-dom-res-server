package com.github.lhervier.domino.oauth.resource;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.github.lhervier.domino.spring.servlet.OsgiDispatcherServlet;
import com.ibm.domino.osgi.core.context.ContextInfo;

/**
 * Servlet extending this class will only be made available at the
 * server root context (IE not when calling servlet alias in a database)
 * @author Lionel hervier
 */
public abstract class BaseOauthResourceServlet extends OsgiDispatcherServlet {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see com.github.lhervier.domino.spring.servlet.OsgiDispatcherServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
		HttpServletResponse response = (HttpServletResponse) resp;
		if( ContextInfo.getUserDatabase() != null ) {
			response.setStatus(404);
			return;
		}
		
		super.service(req, resp);
	}
}
