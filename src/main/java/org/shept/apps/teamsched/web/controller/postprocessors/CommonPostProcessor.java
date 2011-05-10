/**
 * 
 */
package org.shept.apps.teamsched.web.controller.postprocessors;

import java.util.LinkedHashMap;
import java.util.Map;

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
public class CommonPostProcessor extends WebApplicationObjectSupport implements
		ComponentPostprocessor {
	
		/* (non-Javadoc)
		 * @see org.shept.org.springframework.web.bind.support.ComponentPostprocessor#postHandle(org.springframework.web.context.request.WebRequest, org.springframework.web.servlet.ModelAndView, java.lang.String)
		 */
		public void postHandle(WebRequest request, ModelAndView mv,
				String componentPath) {

			if (mv != null) {
				mv.addObject("userRoles", getUserAdminRoles(getMessageSourceAccessor()));
			}
		}
		
		private Map<Integer, String> getUserAdminRoles(MessageSourceAccessor accessor ) {
			Map <Integer, String> modes = new LinkedHashMap <Integer, String>();
			modes.put(0, accessor.getMessage("user")); 
			modes.put(1, accessor.getMessage("administrator")); 
			return modes;
		}


}
