/**
 * 
 */
package org.shept.apps.teamsched.web.support;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.shept.apps.teamsched.orm.Token;
import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.web.controller.IdentController;
import org.shept.org.springframework.orm.hibernate3.support.HibernateDaoSupportExtended;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

/** 
 * @version $$Id: IdentService.java,v 1.1 2009/11/27 18:53:12 oops.oops Exp $$
 *
 * @author Andi
 *
 */
@Transactional
public class IdentService extends ApplicationObjectSupport {
	
	private HibernateDaoSupportExtended dao;
	
	private MailService mailService;
	
	private String serviceUrl;
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW)
	public void requestPasswordChange(HttpServletRequest request, User user, String password) {
		
		Locale locale = RequestContextUtils.getLocale(request);
		
		Token token = new Token();
		token.setToken( UUID.randomUUID().toString());
		token.setDatecreated(GregorianCalendar.getInstance());
		token.setUser(user);
		token.setActionId(FeedbackAction.PASSWORD_CHANGE.getInternalValue());
		token.setProperties(password);
		
		logger.info("Identification request for " + token.toString());
		dao.getHibernateTemplate().saveOrUpdate(token);
		
		EmailFeedback email = new EmailFeedback();
		email.setRecipient(user.getEmail());
		email.setSubject(getMessageSourceAccessor().getMessage("passwordchange", locale));
		email.setAction(FeedbackAction.PASSWORD_CHANGE);
		email.setReferenceData(referenceDataServiceURL(request, token));
		email.setLocale(locale);
		
		mailService.sendConfirmationEmail(email);			
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW)
	public void requestUsernameEmail(HttpServletRequest request, String mailString) {
		Locale locale = RequestContextUtils.getLocale(request);
		
		List<User> result = dao.getHibernateTemplateExtended().
			findByNamedQueryAndNamedParam("qryUserByEmail", "email", mailString.toLowerCase());
		if (result.size() != 1) {
			logger.info("Username request for " + mailString + " not found");
			return;
		}
		
		User user = result.get(0);
		EmailFeedback email = new EmailFeedback();
		email.setRecipient(mailString);
		email.setSubject(getMessageSourceAccessor().getMessage("phrase.requestUsername", locale));
		email.setAction(FeedbackAction.USERNAME_FORGOTTEN);
		email.setLocale(locale);
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("user", user);
//		parms.put("firstName", user.getFirstname());
//		parms.put("lastName", user.getName());
//		parms.put("userName", user.getUsername());
		parms.put("serviceURL", getServiceURL(request));
		email.setReferenceData(parms);
		logger.info("username request for " + mailString + " sent to user " + user.getUsername() );
		
		mailService.sendConfirmationEmail(email);
		
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW)
	public void welcomeUser(HttpServletRequest request, User user, User userHost) {
		
		Locale locale = RequestContextUtils.getLocale(request);
		
		Token token = new Token();
		token.setToken( UUID.randomUUID().toString());
		token.setDatecreated(GregorianCalendar.getInstance());
		token.setUser(user);
		token.setActionId(FeedbackAction.USER_WELCOME.getInternalValue());
		token.setProperties("");
		
		logger.info("Identification request for " + token.toString());
		dao.getHibernateTemplate().saveOrUpdate(token);
		
		EmailFeedback email = new EmailFeedback();
		email.setRecipient(user.getNewemail());
		email.setSubject(getMessageSourceAccessor().getMessage("phrase.welcome", locale));
		email.setAction(FeedbackAction.USER_WELCOME);
		email.setLocale(locale);
		
		Map<String, Object> refData = referenceDataServiceURL(request, token);
		if (userHost != null) {
			refData.put("userHost", userHost);
		}
		email.setReferenceData(refData);
		
		mailService.sendConfirmationEmail(email);
		
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW)
	public void confirmEmailChange(HttpServletRequest request, User user) {
		Locale locale = RequestContextUtils.getLocale(request);
		
		Token token = new Token();
		token.setToken( UUID.randomUUID().toString());
		token.setDatecreated(GregorianCalendar.getInstance());
		token.setUser(user);
		token.setActionId(FeedbackAction.EMAIL_CHANGE.getInternalValue());
		token.setProperties("");
		
		logger.info("Email change confirmation request for " + token.toString());
		dao.getHibernateTemplate().saveOrUpdate(token);
		
		EmailFeedback email = new EmailFeedback();
		email.setRecipient(user.getNewemail());
		email.setSubject(getMessageSourceAccessor().getMessage("phrase.changeEmail", locale));
		email.setAction(FeedbackAction.EMAIL_CHANGE);
		email.setLocale(locale);
		
		Map<String, Object> refData = referenceDataServiceURL(request, token);
		email.setReferenceData(refData);
		
		mailService.sendConfirmationEmail(email);
	
	}

	
	
	/**
	 * Here we execute the users confirmation message to a preceeding mail that has been sent to the user
	 * User clicks on a link in this mail.
	 * 
	 * @param
	 * @return
	 *
	 * @param tok
	 * @param accept
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW)
	public void executeConfirmation(String tok, Boolean accept) {
		Token token = new Token();
		token.setToken(tok);
		token.setDatecreated(null);
		List<Token> tokens = (List<Token>)dao.getHibernateTemplate().findByExample(token);
		if (tokens.size() == 1) {
			token = tokens.get(0);
		} else {
			// possible improvements:
			// log the hosts ip address;
			logger.error("Identification Error for " + tok + " (Could not find token) " );
			return;
		}
		
		Token lastToken = getLastToken(token.getUser(), token.getActionId());
		if (lastToken == null || ! lastToken.equals(token)) {
			logger.error("Execution Error for " + tok + " Only the most recent token can be confirmed " );
			return;			
		}

		FeedbackAction action = FeedbackAction.getFeedbackAction(token.getActionId());
		String changeProperty = token.getProperties();
		switch (action) {
		case EMAIL_CHANGE:
			if (! StringUtils.hasText(token.getUser().getNewemail())) {
				token.setDaterejected(Calendar.getInstance());
				dao.getHibernateTemplate().saveOrUpdate(token);
				logger.info("Requested Email change rejected by system because it contains empty mail address " + token.getUser());		
				return;
			}
			if (accept) {
			token.getUser().setEmail(
					token.getUser().getNewemail());
			token.getUser().setNewemail(null);
			token.setDateaccepted(Calendar.getInstance());
			// token.getUser() ... must be of Cascade.PERSIST
			dao.getHibernateTemplate().saveOrUpdate(token);	// must cascade to user
			logger.info("New Email " + changeProperty + " for User " + token.getUser());
			} else {
				token.setDaterejected(Calendar.getInstance());
				dao.getHibernateTemplate().saveOrUpdate(token);
				logger.info("New Email rejected by user " + token.getUser());
			}
			break;
		case PASSWORD_CHANGE:
			if (! StringUtils.hasText(token.getProperties())) {
				token.setDaterejected(Calendar.getInstance());
				dao.getHibernateTemplate().saveOrUpdate(token);
				logger.info("Requested Password change rejected by system because it contains empty password " + token.getUser());
				return;
			}
			if (accept) {
				token.getUser().setUserpassword(
						token.getProperties());
				token.getUser().setNewpassword(null);
				token.setDateaccepted(Calendar.getInstance());
				// token.getUser() ... must be of Cascade.PERSIST
				dao.getHibernateTemplate().saveOrUpdate(token); // must cascade to user
				logger.info("New password for user " + token.getUser());
			} else {
				token.setDaterejected(Calendar.getInstance());
				dao.getHibernateTemplate().saveOrUpdate(token);
				logger.info("New password rejected by user " + token.getUser());
			}
			break;
		case USER_WELCOME:
			if (! StringUtils.hasText(token.getUser().getNewemail())) {
				token.setDaterejected(Calendar.getInstance());
				dao.getHibernateTemplate().saveOrUpdate(token);
				logger.error("Could not welcome user because of empty mail address " + token.getUser());
				return;
			}
			if (accept) {
				token.getUser().setBactive(true);
				token.getUser().setEmail(token.getUser().getNewemail());
				token.getUser().setNewemail(null);
				token.getUser().setNewpassword(null);
				token.setDateaccepted(Calendar.getInstance());
				// token.getUser() ... must be of Cascade.PERSIST
				dao.getHibernateTemplate().saveOrUpdate(token); // must cascade to user
				logger.info("New user welcome message confirmation " + token.getUser());				
			} else {
				token.getUser().setFdel(token.getUser().getId());
				token.setDaterejected(Calendar.getInstance());
				dao.getHibernateTemplate().saveOrUpdate(token);
				logger.info("User rejected welcome messager " + token.getUser());				
			}
			break;
		default:
			break;
		}
	}
	
	protected Token getLastToken(User user, Integer actionId) {
		return (Token) dao.getHibernateTemplateExtended().
			findObjectByNamedQueryAndNamedParam("qryLastToken",
				new String[]{"userId", "actionId" }, 
				new Object[]{user.getId(), actionId});
	}


	@Resource
	public void setDao(HibernateDaoSupportExtended dao) {
		this.dao = dao;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	
	
	private Map<String, Object> referenceDataServiceURL(HttpServletRequest request, Token token) {
		String serviceURL = getServiceURL(request);
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("serviceURL", serviceURL);
		parms.put("urlResponseOk", 
				getResponseIdentUrl(request, token.getToken(), true));
		parms.put("urlResponseError", 
				getResponseIdentUrl(request, token.getToken(), false));
		parms.put("user", token.getUser());
		return parms;
	}
	
	private String getResponseIdentUrl(HttpServletRequest request, String token, Boolean accept) {
		String queryString = IdentController.getQueryString(token, accept);
		return getServiceURL(request) + "/ident.shept?" + queryString;
	}
	
	/**
	 * Return the application url link to use in emails
	 * serviceUrl needs to be set if we are behind Apache url redirection !
	* @param 
	* @return
	*
	 */
	private String getServiceURL(HttpServletRequest request) {
		if (serviceUrl != null) return serviceUrl;
		StringBuffer buf = request.getRequestURL();
		int idx = buf.lastIndexOf(request.getContextPath());
		return buf.substring(0, idx + request.getContextPath().length());
	}

	/**
	 * @param serviceUrl the serviceUrl to set
	 */
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

}
