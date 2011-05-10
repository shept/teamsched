package org.shept.apps.teamsched.web.controller.factories;

import java.lang.reflect.Method;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.web.controller.commands.UserAdminEditMode;
import org.shept.apps.teamsched.web.controller.commands.UserCommand;
import org.shept.apps.teamsched.web.security.AuthenticationUtils;
import org.shept.org.springframework.web.servlet.mvc.delegation.command.AbstractCommandFactory;
import org.shept.org.springframework.web.servlet.mvc.delegation.configuration.TargetConfiguration;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;


public class UserCommandFactory extends AbstractCommandFactory {
	
	private String relation;

	
	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.servlet.mvc.delegation.command.AbstractCommandFactory#getCommand(org.shept.org.springframework.web.servlet.mvc.delegation.configuration.TargetConfiguration, java.lang.Object)
	 */
	@Override
	public Object getCommand(TargetConfiguration config, Object referencedModel) {
		Object target = referencedModel;
		if (relation != null) {
			String methodGetter = "get" + StringUtils.capitalize(relation);
			Method mth = ReflectionUtils.findMethod(referencedModel.getClass(), methodGetter);
			if (mth == null) {
				logger.error("Configuration error as there is no applicable accessor for '" + relation + "' for object '" + referencedModel.getClass() + "'");
				return null;
			}
			target = ReflectionUtils.invokeMethod(mth, referencedModel);
		}
		if (target == null) {
			return null;		// not a valid user
		}
		UserCommand cmd = new UserCommand();
		User currentUser = (User) AuthenticationUtils.getUser();
		User user = (User) target;
		cmd.setUser(user);

		UserAdminEditMode editMode = null;
		if (user.getId() == null) {
			editMode = UserAdminEditMode.USER_CREATE;			
		} 
		else if (user.equals(currentUser)) {
			editMode = UserAdminEditMode.USER_EDIT;
		}
		else if (currentUser != null && currentUser.getRoleId().equals(1)) {
			editMode = UserAdminEditMode.USER_ADMIN;
		} else {
			return null;
		}
		cmd.setEditMode(editMode);
		return cmd;
	}


	/**
	 * @param relation the relation to set
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}

}
