/**
 * 
 */
package org.shept.apps.teamsched.web.security;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shept.org.springframework.web.servlet.mvc.formcache.SessionFormCache;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class SheptAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private SessionFormCache formCache;

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		super.onAuthenticationSuccess(request, response, authentication);
//		User usr = (User) authentication.getPrincipal();		
		if (formCache != null) {
			formCache.clearCache(request);		// clear the saved forms				
		}
	}

	/**
	 * @param formCache the formCache to set
	 */
	@Resource
	public void setFormCache(SessionFormCache formCache) {
		this.formCache = formCache;
	}

}
