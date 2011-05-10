package org.shept.apps.teamsched.web.controller.commands;

import org.shept.apps.teamsched.orm.User;

/** 
 * @version $$Id: UserValidatableCommand.java,v 1.1 2009/11/27 18:53:10 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public interface UserValidatableCommand {
	
	public abstract UserAdminEditMode getEditMode();
	
	public abstract boolean isNewUser();
	
	public abstract boolean isVerified();

	public abstract User getUser();
	
	public abstract void setUser(User user);
	
	public abstract User getOldUser();
	
	public abstract String getRepeatEmail();

	public abstract String getRepeatCaptcha();
	
	public abstract String getNewPassword();

	public abstract String getRepeatPassword();

}