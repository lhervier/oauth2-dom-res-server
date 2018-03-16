package com.github.lhervier.domino.oauth.resource.sample.controller;

import java.util.ArrayList;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.lhervier.domino.oauth.resource.BearerContext;

@Controller
public class SampleController {

	@Autowired
	private BearerContext bearerCtx;
	
	public static class UsersResponse {
		private List<String> users;
		private String currentUser;
		private List<String> scopes;

		public List<String> getUsers() { return users; }
		public void setUsers(List<String> users) { this.users = users; }
		public String getCurrentUser() { return currentUser; }
		public void setCurrentUser(String currentUser) { this.currentUser = currentUser; }
		public List<String> getScopes() { return scopes; }
		public void setScopes(List<String> scopes) { this.scopes = scopes; }
	}
	
	@RequestMapping("/apis/users")
	public @ResponseBody UsersResponse getUsers(
			@RequestParam(required = false, defaultValue = "") String startsWith, 
			@RequestParam(required = false, defaultValue = "10") int limit) throws NotesException {
		// The aspect ensure that the session is not null
		Session session = this.bearerCtx.getBearerSession();
		Database db = (Database) session.getAddressBooks().get(0);
		if( !db.isOpen() )
			if( !db.open() )
				throw new RuntimeException("Unable to open the NAB");
		
		UsersResponse ret = new UsersResponse();
		ret.setCurrentUser(this.bearerCtx.getAccessToken().getSub());
		ret.setScopes(new ArrayList<String>());
		if( this.bearerCtx.getAccessToken().getScope() != null ) {
			String[] scopes = this.bearerCtx.getAccessToken().getScope().split(" ");
			for( String scope : scopes )
				ret.getScopes().add(scope);
		}
		
		ret.setUsers(new ArrayList<String>(limit));
		
		View v = db.getView("($VIMPeople)");
		ViewEntry entry = v.getEntryByKey(startsWith);
		if( entry == null )
			return ret;
		
		ViewNavigator nav = v.createViewNav();
		if( !nav.gotoEntry(entry) )
			return ret;
		for( int i=0; i<limit; i++ ) {
			if( entry == null )
				break;
			ret.getUsers().add((String) entry.getColumnValues().get(0));
			ViewEntry tmp = nav.getNext();
			entry.recycle();
			entry = tmp;
		}
		return ret;
	}
}
