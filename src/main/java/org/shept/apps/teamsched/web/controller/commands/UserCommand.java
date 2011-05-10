package org.shept.apps.teamsched.web.controller.commands;

import java.io.Serializable;
import java.util.Calendar;

import org.shept.apps.teamsched.orm.User;
import org.shept.org.springframework.beans.support.ModelSupplier;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;


/** 
 * @version $$Id: UserCommand.java,v 1.1 2009/11/27 18:53:10 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class UserCommand implements Serializable, UserValidatableCommand, ModelSupplier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private User oldUser;
	private UserAdminEditMode editMode = UserAdminEditMode.USER_EDIT;

	private String newPassword;
	private String repeatPassword;
	private String repeatEmail;
	private String repeatCaptcha;
	private String forgotUsername;
	
	private boolean verified = true;
	

	public UserAdminEditMode getEditMode() {
		return editMode;
	}
	
	public User getOldUser() {
		return oldUser;
	}
	
	public boolean isNewUser() {
		return editMode.equals(UserAdminEditMode.USER_CREATE) ||
			editMode.equals(UserAdminEditMode.USER_REGISTER);
	}
	
	/**
	 * @return the repeatCaptcha
	 */
	public String getRepeatCaptcha() {
		return repeatCaptcha;
	}
	
	public String getRepeatEmail() {
		return repeatEmail;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public User getUser() {
		return user;
	}

	public void setEditMode(UserAdminEditMode editMode) {
		this.editMode = editMode;
		this.verified = false;
		if (editMode.equals(UserAdminEditMode.USER_EDIT)) {
			this.verified = true;
		} 
	}

	/**
	 * @param repeatCaptcha the repeatCaptcha to set
	 */
	public void setRepeatCaptcha(String repeatCaptcha) {
		this.repeatCaptcha = repeatCaptcha;
	}

	public void setRepeatEmail(String repeatEmail) {
		this.repeatEmail = repeatEmail;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public void setUser(User user) {
		Assert.notNull(user, "A valid user instance is needed");
		this.user = user;
		this.oldUser = (User) BeanUtils.instantiateClass(User.class);
		BeanUtils.copyProperties(user, oldUser);
	}

	public Object getModel() {
		return getUser();
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the forgotUsername
	 */
	public String getForgotUsername() {
		return forgotUsername;
	}

	/**
	 * @param forgotUsername the forgotUsername to set
	 */
	public void setForgotUsername(String forgotUsername) {
		this.forgotUsername = forgotUsername;
	}

	/**
	 * @return the bterms
	 */
	public Boolean getBterms() {
		return user.getDatTerms() != null;
	}

	/**
	 * @param bterms the bterms to set
	 */
	public void setBterms(Boolean bterms) {
		user.setDatTerms(bterms ? Calendar.getInstance() : null);			
	}

	/**
	 * @return the verified
	 */
	public boolean isVerified() {
		return verified;
	}

	public void verifiedOk() {
		this.verified = true;
	}

}