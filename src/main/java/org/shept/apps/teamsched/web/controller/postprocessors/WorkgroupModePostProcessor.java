/**
 * 
 */
package org.shept.apps.teamsched.web.controller.postprocessors;

import java.util.LinkedHashMap;
import java.util.Map;

import org.shept.apps.teamsched.web.controller.commands.WorkgroupSelectMode;
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
public class WorkgroupModePostProcessor extends WebApplicationObjectSupport
		implements ComponentPostprocessor {

	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.bind.support.ComponentPostprocessor#postHandle(org.springframework.web.context.request.WebRequest, org.springframework.web.servlet.ModelAndView, java.lang.String)
	 */
	public void postHandle(WebRequest request, ModelAndView mv,
			String componentPath) {
		if (mv != null) {
			mv.addObject("workgroupModeSelections", getGroupModes(getMessageSourceAccessor()));
		}
	}

	protected Map<WorkgroupSelectMode, String> getGroupModes(MessageSourceAccessor accessor ) {
		Map <WorkgroupSelectMode, String> modes = new LinkedHashMap <WorkgroupSelectMode, String>();
		modes.put(WorkgroupSelectMode.GROUPS_OWNED, accessor.getMessage("workgroupMode.groupsOwned"));
		modes.put(WorkgroupSelectMode.GROUPS_MEMBER, accessor.getMessage("workgroupMode.groupsMember"));
		modes.put(WorkgroupSelectMode.GROUPS_INVITED, accessor.getMessage("workgroupMode.groupsInvited"));
		return modes;
	}

}
