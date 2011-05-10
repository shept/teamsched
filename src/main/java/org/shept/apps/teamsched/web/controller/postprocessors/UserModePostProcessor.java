/**
 * 
 */
package org.shept.apps.teamsched.web.controller.postprocessors;

import java.util.LinkedHashMap;
import java.util.Map;

import org.shept.apps.teamsched.web.controller.commands.UserAdminSelectionMode;
import org.shept.org.springframework.web.bind.support.ComponentPostprocessor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.ModelAndView;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class UserModePostProcessor extends WebApplicationObjectSupport implements
		ComponentPostprocessor {
	
	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.bind.support.ComponentPostprocessor#postHandle(org.springframework.web.context.request.WebRequest, org.springframework.web.servlet.ModelAndView, java.lang.String)
	 */
	public void postHandle(WebRequest request, ModelAndView mv,
			String componentPath) {

		if (mv != null) {
			mv.addObject("userModeSelections", getUserModes(getMessageSourceAccessor()));
		}
	}
	
	private Map<UserAdminSelectionMode, String> getUserModes(MessageSourceAccessor accessor ) {
		Map <UserAdminSelectionMode, String> modes = new LinkedHashMap <UserAdminSelectionMode, String>();
		modes.put(UserAdminSelectionMode.USER_MYSELF, accessor.getMessage("userMode.myOwnUser")); 
		modes.put(UserAdminSelectionMode.USERS_HOSTED, accessor.getMessage("userMode.userHosted")); 
		modes.put(UserAdminSelectionMode.USERS_INVITATIONS, accessor.getMessage("userMode.userInvitations")); 
		return modes;
	}

}
