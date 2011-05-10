package org.shept.apps.teamsched.web.controller.factories;

import javax.servlet.http.HttpServletRequest;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.web.controller.commands.UserConfirmTermsCommand;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentToken;
import org.shept.org.springframework.web.servlet.mvc.delegation.command.CommandFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationObjectSupport;

public class UserConfirmTermsCommandFactory extends WebApplicationObjectSupport
		implements CommandFactory {

	public Object getCommand(HttpServletRequest request, ComponentToken token) {
		UserConfirmTermsCommand cmd = new UserConfirmTermsCommand(true);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (null != auth && null != auth.getPrincipal() && (auth.getPrincipal() instanceof User)) {
			User usr = (User) auth.getPrincipal();
			cmd.setBterms(null != usr.getDatTerms());
		}
		return cmd;
	}

}
