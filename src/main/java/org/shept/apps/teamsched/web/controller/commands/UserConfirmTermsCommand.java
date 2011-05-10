/**
 * 
 */
package org.shept.apps.teamsched.web.controller.commands;

import java.io.Serializable;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class UserConfirmTermsCommand implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean bterms;

	/**
	 * 
	 */
	public UserConfirmTermsCommand() {
		super();
	}

	/**
	 * @param bterms
	 */
	public UserConfirmTermsCommand(Boolean bterms) {
		super();
		this.bterms = bterms;
	}

	/**
	 * @return the bterms
	 */
	public Boolean getBterms() {
		return bterms;
	}

	/**
	 * @param bterms the bterms to set
	 */
	public void setBterms(Boolean bterms) {
		this.bterms = bterms;
	}

}
