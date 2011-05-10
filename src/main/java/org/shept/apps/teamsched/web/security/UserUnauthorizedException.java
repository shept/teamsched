/**
 * 
 */
package org.shept.apps.teamsched.web.security;

import org.springframework.security.core.AuthenticationException;


/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class UserUnauthorizedException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 */
	public UserUnauthorizedException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 * @param t
	 */
	public UserUnauthorizedException(String msg, Throwable t) {
		super(msg, t);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 * @param extraInformation
	 */
	public UserUnauthorizedException(String msg, Object extraInformation) {
		super(msg, extraInformation);
		// TODO Auto-generated constructor stub
	}

}
