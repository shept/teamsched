/**
 * 
 */
package org.shept.apps.teamsched.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class SheptAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static String LOGIN_ERR_PARAM = "login_error"; 	// used in login.jspf also

	private String defaultFailureUrl;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
        redirectStrategy.sendRedirect(request, response, determineFailureUrl(exception));
	}

	protected String determineFailureUrl(AuthenticationException failed) {
		String url =  defaultFailureUrl;
		// cutoff any existing parameters
		int cutIdx = url.indexOf("?");
		if (cutIdx > 0) {
			url = url.substring(0, cutIdx);
		}
		url = url + "?" + LOGIN_ERR_PARAM + "=";
		if (failed instanceof UserUnauthorizedException) {
			return url + "2";
		}
		return url+"1";
	}

	/**
	 * @param defaultFailureUrl the defaultFailureUrl to set
	 */
	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}

	/**
	 * @param redirectStrategy the redirectStrategy to set
	 */
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

}
