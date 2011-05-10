/**
 * 
 */
package org.shept.apps.teamsched.web.security;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shept.apps.teamsched.orm.LoginLog;
import org.shept.org.springframework.orm.hibernate3.support.HibernateDaoSupportExtended;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	private HibernateDaoSupportExtended dao;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		if (authentication != null ) {
			WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
			
//			User usr = (User) authentication.getPrincipal();
//			dao.getHibernateTemplateExtended().refresh(usr);
			
			// we might save other user information here in the database

			LoginLog log = findLastLogin(details.getSessionId());
			if (log == null ) {
				return;	// throw exception ???
			}
			log.setDateLogout(Calendar.getInstance());
			dao.getHibernateTemplateExtended().saveOrUpdate(log);
			dao.getHibernateTemplateExtended().flush();
		}
		getRedirectStrategy().sendRedirect(request, response, getDefaultTargetUrl());
	}

	@SuppressWarnings("unchecked")
	public LoginLog findLastLogin(String sessionId) {
		List login = dao.getHibernateTemplateExtended().findByNamedQueryAndNamedParam("qryLoginLog", "sessionId", sessionId);
		if (login.size() == 0 ) return null;
		return (LoginLog) login.get(0);
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(HibernateDaoSupportExtended dao) {
		this.dao = dao;
	}
	

}
