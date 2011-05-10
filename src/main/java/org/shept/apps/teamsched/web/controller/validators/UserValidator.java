package org.shept.apps.teamsched.web.controller.validators;

import org.shept.apps.teamsched.web.controller.commands.UserAdminEditMode;
import org.shept.apps.teamsched.web.controller.commands.UserValidatableCommand;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentUtils;
import org.shept.util.StringUtilsExtended;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

/** 
 * @version $$Id: UserValidator.java,v 1.1 2009/11/27 18:53:10 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class UserValidator extends AbstractUserValidator {

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors, String path) {
		
		UserValidatableCommand fbo = (UserValidatableCommand) ComponentUtils.getComponent(target, path);

		int errCnt = errors.getErrorCount();

		/*
		 * Check the captcha first
		 * If captcha doesn't validate ok we will not process any other checks because nobody should
		 * play try-and-error tricks with it. Captcha will not be checked if the own account is edited
		 */
		if (! fbo.isVerified()) {
			if (! validateCaptcha(fbo, errors, path)) return;			
		}
		
		/*
		 * Check the presence of all required fields 
		 */
		if ( !StringUtils.hasText(fbo.getUser().getUsername())) {
			errors.rejectValue("user.username", "userForm.usernameEmpty");
		}

		if (!StringUtils.hasText(fbo.getUser().getFirstname())) {
			errors.rejectValue("user.firstname",
					"userForm.firstnameEmpty");
		}

		if (!StringUtils.hasText(fbo.getUser().getName())) {
			errors.rejectValue("user.name",
					"userForm.nameEmpty");
		}

		/*
		 * Checking all fields for confirmation (currently new password and new email)
		 */
		if (! StringUtilsExtended.equalsIgnoreNullsAndCase(fbo.getUser().getNewemail(), fbo.getRepeatEmail())) {
			errors.rejectValue("user.newemail",
			"userForm.emailValidation");
			errors.rejectValue("repeatEmail",
			"userForm.emailValidation");			
		}
		
		if (! StringUtilsExtended.equalsIgnoreNulls(fbo.getNewPassword(), fbo.getRepeatPassword())) {
			errors.rejectValue("newPassword",
			"userForm.passwordConfirmation");
			errors.rejectValue("repeatPassword",
			"userForm.passwordConfirmation");						
		}
		
		if (fbo.getEditMode().equals(UserAdminEditMode.USER_REGISTER) && 
				fbo.getUser().getDatTerms() == null) {
			errors.rejectValue("bterms", "login.confirmTermsAndConditions");
		}
		
		// checking username and email depending on the different editing conditions
		
		Long usernameCount = (Long) dao.getHibernateTemplateExtended().findObjectByNamedQueryAndNamedParam(
				"qryUsernameCount", "name", fbo.getUser().getUsername());
		
		Long  emailCount = 0L;
		if (StringUtils.hasText(fbo.getUser().getNewemail())) {
			if (!validateEmail(fbo.getUser().getNewemail())) {
				errors.rejectValue("user.newemail", "userForm.emailInvalid");						
			}
			emailCount = (Long) dao.getHibernateTemplateExtended().findObjectByNamedQueryAndNamedParam(
					"qryEmailCount", "email",fbo.getUser().getNewemail());
		}

		boolean duplicate = false;

		// In CREATE-mode and REGISTRATION-mode we create a new user.
		// This is either a self registration or somebody creates another user
		if (fbo.isNewUser()) {
			if (fbo.getUser().getId() == null) {
				// the user is not yet saved
				if (! StringUtils.hasText(fbo.getUser().getNewemail())) {
					errors.rejectValue("user.newemail", "userForm.emailEmpty");
				} 
				if (!StringUtils.hasText(fbo.getNewPassword())) {
					errors.rejectValue("newPassword",
							"userForm.passwordEmpty");
				}
				if (emailCount > 0 || usernameCount > 0)  { duplicate = true;}
			} else {
				// the user is already saved, reedit  user which was already created
				if (StringUtils.hasText(fbo.getUser().getNewemail())) {
					if ( ! fbo.getUser().getNewemail().equals(fbo.getOldUser().getNewemail())) {
						if (emailCount > 0 ) { duplicate = true ; }
					}
				}
				if (! fbo.getUser().getUsername().equals(fbo.getOldUser().getUsername())) {
					if (usernameCount > 0) { duplicate = true ; }
				}
			}
		}
		// In EDIT-mode you edit your own account or - as an administrator - sombebody elses data
		else if  (fbo.getEditMode().equals(UserAdminEditMode.USER_EDIT)) {
			// check if username or password have changed
			 if (! fbo.getUser().getUsername().equals(fbo.getOldUser().getUsername())) {
					if (usernameCount > 0 ) { duplicate = true; }
			 }
			 if ( ! StringUtilsExtended.equalsIgnoreNulls(fbo.getUser().getNewemail(), fbo.getUser().getEmail())) {
					if (emailCount > 0) { duplicate = true; }
			 }
		}

		if (duplicate) {
				// flag both errors so that user cannot guess other peoples passwords / emails
				errors.rejectValue("user.username", "userForm.usernameEmailExists");				
				errors.rejectValue("user.newemail", "userForm.usernameEmailExists");				 
		 }

		String logString = "User Form Validation for '"
				+ fbo.getUser().getUsername() + "' Id: "
				+ fbo.getUser().getIdentifier();
		if (errors.getErrorCount() - errCnt > 0) {
			logger.warn(logString + " Failed ");
			logger.debug(errors.getAllErrors());
		} else {
			logger.info(logString + " Succeeded");
		}
	}	

}
