/**
 * 
 */
package org.shept.apps.teamsched.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.shept.apps.teamsched.web.controller.commands.UserAdminSelectionMode;
import org.shept.apps.teamsched.web.controller.filters.UserModeFilter;
import org.shept.org.springframework.beans.support.CommandWrapper;
import org.shept.org.springframework.beans.support.DefaultCommandObject;
import org.shept.org.springframework.beans.support.Refreshable;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentUtils;
import org.shept.org.springframework.web.servlet.mvc.delegation.MultiActionController;
import org.shept.util.PageHolderFactory;

/**
 * @author lucid64
 *
 */
public class UserModeController extends MultiActionController {

	private PageHolderFactory pageHolderFactory;

	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.servlet.mvc.delegation.MultiActionController#buildCommandObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object buildCommandObject(HttpServletRequest request) {
		DefaultCommandObject co = new DefaultCommandObject();
		co.getChildren().clear();

		UserModeFilter filter = new UserModeFilter();
		filter.setMode(UserAdminSelectionMode.USERS_HOSTED);
		CommandWrapper cw = new CommandWrapper();
		cw.setCommand(filter);	
		cw.setTagName("userMode");
		ComponentUtils.applyConfiguration(cw, getWebApplicationContext());
		co.getChildren().add(cw);		

		// only works for USERS_HOSTED !
		Refreshable listHolder = pageHolderFactory.getObject();
		listHolder.setFilter(filter);

		cw = new CommandWrapper();
		cw.setCommand(listHolder);
		cw.setTagName("userInvitations");
		co.getChildren().add(cw);
		ComponentUtils.applyConfiguration(cw, getWebApplicationContext());
		listHolder.refresh();
		return co;
	}

	/**
	 * @param pageHolderFactory the pageHolderFactory to set
	 */
	@Resource
	public void setPageHolderFactory(PageHolderFactory pageHolderFactory) {
		this.pageHolderFactory = pageHolderFactory;
	}

	
}
