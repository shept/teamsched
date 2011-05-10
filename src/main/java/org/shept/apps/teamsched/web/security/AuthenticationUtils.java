/**
 * 
 */
package org.shept.apps.teamsched.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author lucid64
 *
 */
public class AuthenticationUtils {

	public static UserDetails getUser() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = null;
		if (context.getAuthentication() == null) {
			return null;
		}
		auth = context.getAuthentication();
		if (auth.getPrincipal() == null) {
			return null;
		}
		// getAuthentication().getPrincipal() might return a String 'anonymousUser' e.t.c.
		if (! UserDetails.class.isAssignableFrom(auth.getPrincipal().getClass())) { 
			return null;
		}
		return (UserDetails) auth.getPrincipal();
	}


}
