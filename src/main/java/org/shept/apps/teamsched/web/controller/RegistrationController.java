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
public class RegistrationController extends MultiActionController {

	@Override
	protected Object buildCommandObject(HttpServletRequest request) {
		DefaultCommandObject co = new DefaultCommandObject();
		co.getChildren().clear();

		UserCommand cmd = new UserCommand();
		User usr = User.getInstance();
		cmd.setUser(usr);
		cmd.setEditMode(UserAdminEditMode.USER_REGISTER);
		
		CommandWrapper cw = new CommandWrapper();
		cw.setCommand("");	// dummy object - should be serializable - workaround for null pointers
		cw.setTagName("wizard");
		co.getChildren().add(cw);		

		cw = new CommandWrapper();
		cw.setCommand(cmd);
		cw.setTagName("userForm");

		co.getChildren().add(cw);
		ComponentUtils.applyConfiguration(cw, getWebApplicationContext());
		return co;
	}

}
