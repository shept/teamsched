/**
 * 
 */
package org.shept.apps.teamsched.web.support;

import java.util.Locale;
import java.util.Map;

/** 
 * @version $$Id: EmailFeedback.java,v 1.1 2009/11/27 18:53:12 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class EmailFeedback {
	
	private String recipient;
	
	private String subject;
	
	private Map <String, Object> referenceData;

	private FeedbackAction action;
	
	private Locale locale;

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, Object> getReferenceData() {
		return referenceData;
	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	public FeedbackAction getAction() {
		return action;
	}

	public void setAction(FeedbackAction action) {
		this.action = action;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	
}
