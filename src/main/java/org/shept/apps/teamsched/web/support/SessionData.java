package org.shept.apps.teamsched.web.support;


/** 
 * @version $$Id: SessionData.java,v 1.1 2009/11/27 18:53:12 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class SessionData {
	
	// a unique id identifying the session
	// it would be prefarable to somehow inject the session id (request.getSession.getId())
	private String sessionId;	// = new Random().nextInt();		
	
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


}
