package com.github.lhervier.domino.oauth.resource;

import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.lhervier.domino.spring.servlet.NotesContext;
import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.Os;
import com.ibm.domino.napi.c.xsp.XSPNative;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;

public class BearerContext {

	/**
	 * Logger
	 */
	private static final Log LOG = LogFactory.getLog(BearerContext.class);
	
	/**
	 * The id token service
	 */
	@Autowired
	private AccessTokenService tokenSvc;
	
	/**
	 * The http request
	 */
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * The notes context
	 */
	@Autowired
	private NotesContext notesCtx;
	
	/**
	 * Delegated
	 */
	private Session bearerSession;

	/**
	 * The usernamelist
	 */
	private Long userNameList;
	
	/**
	 * The id token
	 */
	public AccessToken accessToken;
	
	/**
	 * Bean initialization
	 * @throws InvalidTokenException if the token is invalid
	 */
	@PostConstruct
	public void init() throws InvalidTokenException {
		this.bearerSession = null;
		try {
			// Extract bearer token
			String auth = this.request.getHeader("Authorization");
			if( auth == null )
				throw new InvalidTokenException("Authorization header is mandatory");
			if( !auth.startsWith("Bearer ") )
				throw new InvalidTokenException("Authorization header must use the Bearer schema");
			String accessToken = auth.substring("Bearer ".length());
			if( Utils.isEmpty(accessToken) )
				throw new InvalidTokenException("Token cannot be empty");
			
			// Get the id_token
			this.accessToken = this.tokenSvc.getIdToken(accessToken);
			
			// Check sign key
			if( this.accessToken == null ) {
				LOG.error("unable to get id token from access token...");
				throw new InvalidTokenException("Access token is invalid");
			}
			
			// Get the user name in the subject
			String sub = this.accessToken.getSub();
			
			// Get the username from the subject
			String userName = this.getFullName(sub);
			
			// Create session
			this.userNameList = NotesUtil.createUserNameList(userName);
			this.bearerSession = XSPNative.createXPageSession(userName, this.userNameList, false, false);
		} catch (NotesException e) {
			throw new RuntimeException(e);
		} catch (NException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Return the full name of a user from one of its name
	 */
	@SuppressWarnings("unchecked")
	private String getFullName(String sub) throws NotesException {
		Vector<Database> nabs = this.notesCtx.getServerSession().getAddressBooks();
		for( Database nab : nabs ) {
			if( !nab.isOpen() )
				if( !nab.open() )
					throw new RuntimeException("Unable to open nab '" + nab.getFilePath() + "'");
			View v = null;
			Document doc = null;
			try {
				v = nab.getView("($Users)");
				doc = v.getDocumentByKey(sub, true);
				if( doc != null )
					return doc.getItemValueString("FullName");
			} finally {
				Utils.recycleQuietly(doc);
				Utils.recycleQuietly(v);
			}
		}
		return null;
	}
	
	/**
	 * Bean cleaup
	 */
	@PreDestroy
	public void cleanUp() {
		if( this.bearerSession != null ) {
			Utils.recycleQuietly(this.bearerSession);
			this.bearerSession = null;
		}
		
		if( this.userNameList != null ) {
			try {
				Os.OSMemFree(this.userNameList);
			} catch (NException e) {
				throw new RuntimeException(e);
			}
			this.userNameList = null;
		}
	}
	
	/**
	 * Return the bearer session
	 */
	public Session getBearerSession() {
		return this.bearerSession;
	}

	/**
	 * @return the accessToken
	 */
	public AccessToken getAccessToken() {
		return accessToken;
	}
}
