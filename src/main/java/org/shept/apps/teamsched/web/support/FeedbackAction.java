/**
 * 
 */
package org.shept.apps.teamsched.web.support;

import java.util.HashMap;

/** 
 * @version $$Id: FeedbackAction.java,v 1.1 2009/11/27 18:53:12 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public enum FeedbackAction {
	
	PASSWORD_CHANGE(0, "changePassword.vm"),
	EMAIL_CHANGE(1, "emailChange.vm"),
	USER_WELCOME(2, "welcomeUser.vm"),
	USERNAME_FORGOTTEN(3,"requestUsername.vm");
	
	private int internalValue;
	
	private String template;
	
	private static HashMap<Integer, FeedbackAction> enumsByInternalValue;
	
	
	/**
	 * @param internalValue
	 */
	private FeedbackAction(int internalValue, String template) {
		this.internalValue = internalValue;
		this.template = template;
	}

	/*
	 * better use seperate interval value instead of ordinal
	 * because 'ordinal' is not a getter and ordinal
	 * numbering might change depending on future additions to the list
	 */
	public int getInternalValue() {
		return internalValue;
	}

	public String getTemplate() {
		return template;
	}
	
	public static FeedbackAction getFeedbackAction(Integer idx) {
		if (enumsByInternalValue == null ) {
			enumsByInternalValue = new HashMap<Integer, FeedbackAction>();
			for (FeedbackAction action : FeedbackAction.values()) {
				enumsByInternalValue.put(action.getInternalValue(), action);
			} 
		}
		return enumsByInternalValue.get(idx);
	}
	


}
