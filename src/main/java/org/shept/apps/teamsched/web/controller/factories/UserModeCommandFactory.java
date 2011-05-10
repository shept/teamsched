/**
 * 
 */
package org.shept.apps.teamsched.web.controller.factories;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.shept.apps.teamsched.web.controller.commands.UserAdminEditMode;
import org.shept.apps.teamsched.web.controller.commands.UserAdminSelectionMode;
import org.shept.apps.teamsched.web.controller.commands.UserCommand;
import org.shept.apps.teamsched.web.controller.filters.UserModeFilter;
import org.shept.org.springframework.beans.support.CommandWrapper;
import org.shept.org.springframework.beans.support.Refreshable;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentToken;
import org.shept.org.springframework.web.servlet.mvc.delegation.command.CommandFactory;
import org.shept.util.PageHolderFactory;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author lucid64
 *
 */
public class UserModeCommandFactory implements CommandFactory {
	
	private PageHolderFactory pageHolderFactory;


	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.servlet.mvc.delegation.command.CommandFactory#getCommand(javax.servlet.http.HttpServletRequest, org.shept.org.springframework.web.servlet.mvc.delegation.ComponentToken)
	 */
	public Object getCommand(HttpServletRequest request, ComponentToken token) {
		token.getBinder().bind(new ServletWebRequest(request));
		UserModeFilter filter = ((UserModeFilter) token.getComponent());
		
		if (filter.getMode().equals(UserAdminSelectionMode.USER_MYSELF)) {
			UserCommand cmd = new UserCommand();
			cmd.setUser(filter.getUser());
			cmd.setEditMode(UserAdminEditMode.USER_EDIT);
			CommandWrapper cw = new CommandWrapper();
			cw.setCommand(cmd);
			cw.setTagName("userForm");
			return cw;
		}
		else {
			Refreshable listHolder = getPageHolderFactory().getObject();
			listHolder.setFilter(filter);
			return listHolder;
		}
	}


	/**
	 * @return the pageHolderFactory
	 */
	public PageHolderFactory getPageHolderFactory() {
		return pageHolderFactory;
	}


	/**
	 * @param pageHolderFactory the pageHolderFactory to set
	 */
	@Resource
	public void setPageHolderFactory(PageHolderFactory pageHolderFactory) {
		this.pageHolderFactory = pageHolderFactory;
	}

}
