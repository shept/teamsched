package org.shept.apps.teamsched.web.security;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shept.apps.teamsched.orm.LoginLog;
import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.UserAgent;
import org.shept.apps.teamsched.orm.UserScreen;
import org.shept.org.springframework.orm.hibernate3.support.HibernateDaoSupportExtended;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.util.ClassUtils;

/** 
 * @version $$Id: LoggerListener.java,v 1.1 2009/11/27 18:53:24 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class LoggerListener extends org.springframework.security.authentication.event.LoggerListener {

	/** Logger that is available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	private HibernateDaoSupportExtended dao;
	
	private static int maxNameLength = 64;

	@Override
	public void onApplicationEvent(AbstractAuthenticationEvent event) {
//		super.onApplicationEvent(event);

		if (event instanceof AbstractAuthenticationEvent) {
			AbstractAuthenticationEvent authEvent = (AbstractAuthenticationEvent) event;
			Authentication auth = authEvent.getAuthentication();

			String message = "Authentication event " + ClassUtils.getShortName(authEvent.getClass()) + ": "
			+ authEvent.getAuthentication().getName() + "; details: "
			+ authEvent.getAuthentication().getDetails();

			if (event instanceof AbstractAuthenticationFailureEvent) {
				message = message + "; exception: "
				+ ((AbstractAuthenticationFailureEvent) event).getException().getMessage();
			}

			if (event  instanceof AuthenticationSuccessEvent) {
				User usr = (User) authEvent.getAuthentication().getPrincipal();
				WebAuthenticationDetails det = (WebAuthenticationDetails) authEvent.getAuthentication().getDetails();
				saveLogin(usr, Boolean.TRUE, auth, det);
			}

			if (event  instanceof AuthenticationFailureBadCredentialsEvent) {
				WebAuthenticationDetails det = (WebAuthenticationDetails) authEvent.getAuthentication().getDetails();
				saveLogin(null, Boolean.FALSE, auth, det);
			}

			logger.info(message);
		}
	}

	/**
	* @param log
	* @param auth
	* @param usr
	* @param det
	*/
	protected void saveLogin(User usr, Boolean bSuccess, Authentication auth,
			WebAuthenticationDetails det) {
		LoginLog login = new LoginLog();
		if (bSuccess) {
			// need to look for existing session in case user logs on again
			LoginLog lastLog = findLastLogin(det.getSessionId());
			if (lastLog != null) {
				login = lastLog;
			}
			login.setSessionId(det.getSessionId());
		} else {
			login.setSessionId(null); // default is empty session, else we
										// would not catch multiple login failures
		}
		login.setUser(usr);
		login.setDateLogin(Calendar.getInstance());
		login.setUserName(auth.getName());
		login.setUserPassword((String) auth.getCredentials());
		login.setBsuccess(bSuccess);
		login.setRemoteAddr(det.getRemoteAddress());
		login.setRemoteHost(det.getRemoteAddress());

		if (det.getUserAgent() != null) {
			UserAgent agent = findUserAgent(det.getUserAgent());
			if (agent == null) {
				agent = new UserAgent();
				agent.setUserAgent(det.getUserAgent());
				// Only for Providing some info about new agent, row should be edited by admin
				agent.setName("Unclassified: " + agent.getUserAgent());
				if (agent.getName().length() > maxNameLength) {
					agent.setName(agent.getName().substring(0, maxNameLength));
				}
				dao.getHibernateTemplateExtended().saveOrUpdate(agent);
			}
			login.setAgent(agent);
		}
		if (det.getScreenResolution() != null) {
			UserScreen screen = findUserScreen(det.getScreenResolution());
			if (screen == null) {
				screen = new UserScreen();
				screen.setScreenResolution(det.getScreenResolution());
				// Only for Providing some info about new screen, row should be edited by admin
				screen.setName("Unclassified: " + screen.getScreenResolution());
				if (screen.getName().length() > maxNameLength) {
					screen.setName(screen.getName().substring(0, maxNameLength));
				}
				dao.getHibernateTemplateExtended().saveOrUpdate(screen);
			}
			login.setScreen(screen);
		}
		String remoteHost = det.getRemoteAddress();
		if (det.getHostAddressForwarded() != null) {
			remoteHost = det.getHostAddressForwarded() + " ||  " + remoteHost;
			login.setRemoteAddr(remoteHost);
		}

		dao.getHibernateTemplateExtended().saveOrUpdate(login);
		dao.getHibernateTemplateExtended().flush();
	}
	
	
	@SuppressWarnings("unchecked")
	public LoginLog findLastLogin(String sessionId) {
		List login = dao.getHibernateTemplateExtended().findByNamedQueryAndNamedParam("qryLoginLog", "sessionId", sessionId);
		if (login.size() == 0 ) return null;
		return (LoginLog) login.get(0);
	}

	public UserAgent findUserAgent(String agent) {
		return (UserAgent) dao.getHibernateTemplateExtended().
			findObjectByNamedQueryAndNamedParam("qryUserAgent", "userAgent", agent);
	}
	
	public UserScreen findUserScreen(String resolution) {
		return (UserScreen) dao.getHibernateTemplateExtended().
		findObjectByNamedQueryAndNamedParam("qryUserScreen", "resolution", resolution);
	}


	/**
	 * @param dao the dao to set
	 */
	public void setDao(HibernateDaoSupportExtended dao) {
		this.dao = dao;
	}

}
