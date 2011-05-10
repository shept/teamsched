/**
 * 
 */
package org.shept.apps.teamsched.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.web.controller.commands.UserAdminEditMode;
import org.shept.apps.teamsched.web.controller.commands.UserCommand;
import org.shept.org.springframework.beans.support.CommandWrapper;
import org.shept.org.springframework.beans.support.DefaultCommandObject;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentUtils;
import org.shept.org.springframework.web.servlet.mvc.delegation.MultiActionController;

/**
 * @version $$Id: $$
 * 
 * @author Andi
 * 
 */
public class PasswordForgottenController extends MultiActionController {

	/**
	 * 
	 */
	@Override
	protected Object buildCommandObject(HttpServletRequest request) {
		DefaultCommandObject co = new DefaultCommandObject();
		UserCommand cmd = new UserCommand();
		cmd.setUser(User.getInstance());
		cmd.setEditMode(UserAdminEditMode.USER_FORGOT_PASSWORD);
		CommandWrapper cw = new CommandWrapper();
		cw.setCommand(cmd);
		cw.setTagName("passwordChange");
		co.getChildren().clear();
		co.getChildren().add(cw);
		ComponentUtils.applyConfiguration(cw, getWebApplicationContext());
		return co;
	}

	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.servlet.mvc.delegation.MultiActionController#createCommandObject(javax.servlet.http.HttpServletRequest, java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Object getCommandObject(HttpServletRequest request, Class clazz)
			throws Exception {
		DefaultCommandObject co = (DefaultCommandObject) super.getCommandObject(request, clazz);
		CommandWrapper cw = co.getChildren().get(0);
		UserCommand cmd = (UserCommand) cw.getCommand();
		cmd.setRepeatCaptcha("");
		return co;
	}

}
