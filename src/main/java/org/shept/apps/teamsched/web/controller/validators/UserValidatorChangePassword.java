package org.shept.apps.teamsched.web.controller.validators;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.web.controller.commands.UserValidatableCommand;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

/** 
 * @version $$Id: UserValidator.java,v 1.1 2009/11/27 18:53:10 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class UserValidatorChangePassword extends AbstractUserValidator {

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors, String path) {
		
		UserValidatableCommand fbo = (UserValidatableCommand) ComponentUtils.getComponent(target, path);

		// check the captcha
		if (! validateCaptcha(fbo, errors, path)) return;

		if ( !StringUtils.hasText(fbo.getUser().getUsername())) {
			errors.rejectValue("user.username", "userForm.usernameEmpty");
		}
		
		User searchUser = (User) dao.getHibernateTemplateExtended()
					.findObjectByNamedQueryAndNamedParam(
							"qryUserByUsernameAndEmail",
							new String[] { "name", "email" },
							new Object[] { fbo.getUser().getUsername(),
									fbo.getRepeatEmail() });
			if (searchUser == null) {
				errors.rejectValue("user.username",
						"userForm.usernameEmailMissing");
				errors.rejectValue("repeatEmail",
						"userForm.usernameEmailMissing");
			} else {
				// so that we have found the user we replace the backing object
				// with this one
				fbo.setUser(searchUser);
				if (!StringUtils.hasText(fbo.getNewPassword())) {
					errors.rejectValue("newpassword",
							"userForm.passwordEmpty");
				}
				else if  ( ! ObjectUtils.nullSafeEquals( fbo.getRepeatPassword(), fbo.getNewPassword())) {
						errors.rejectValue("newPassword", "userForm.passwordConfirmation");
						errors.rejectValue("repeatPassword", "userForm.passwordConfirmation");
					}
			}

		
		String logString = "Password forgotten request for '" + fbo.getUser().getUsername() + "' Id: " + fbo.getUser().getIdentifier();
			logger.info(logString + " succeessfully processed");
	}
	

}
