/**
 * 
 */
package org.shept.apps.teamsched.web.controller.postprocessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.view.CountObject;
import org.shept.beans.support.FilterDefinition;
import org.shept.beans.support.QueryDefinition;
import org.shept.org.springframework.beans.support.Refreshable;
import org.shept.org.springframework.orm.hibernate3.support.HibernateDaoSupportExtended;
import org.shept.org.springframework.web.bind.support.ComponentPostprocessor;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.ModelAndView;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class UserInvitationsPostProcessor extends WebApplicationObjectSupport implements
		ComponentPostprocessor {
	
	private HibernateDaoSupportExtended dao;

	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.bind.support.ComponentPostprocessor#postHandle(org.springframework.web.context.request.WebRequest, org.springframework.web.servlet.ModelAndView, java.lang.String)
	 */
	public void postHandle(WebRequest request, ModelAndView mv,
			String componentPath) {
		Object component = ComponentUtils.getComponent(mv, componentPath);
		String query = "qryWorkgroupCountForInvitationByHost";

		if (mv != null) {	
			FilterDefinition filterDef =  ((Refreshable) component).getFilter();
			if (filterDef instanceof QueryDefinition) {
				if ( ! ((QueryDefinition) filterDef).getQuery().equals("qryUserByHost")) {
					query="qryWorkgroupCountForInvitationByInvitation";					
				}
			}

			List<CountObject> wgCount =(List<CountObject>) ((HibernateDaoSupportExtended) getDao()).getHibernateTemplate().
				findByNamedQueryAndNamedParam(query, "userHostId", getUserId());
			
			Map<Long, Long> wgCountMap = new HashMap<Long, Long>();
			for (CountObject countObject : wgCount) {
				wgCountMap.put(countObject.getId(), countObject.getCount());
			}
			mv.addObject("wgCount", wgCountMap);
		}
	}

	public Integer getUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() == null || ! (auth.getPrincipal() instanceof User)) {
			return null;
		}
		return ((User) auth.getPrincipal()).getId();
	}

	/**
	 * @return the dao
	 */
	public HibernateDaoSupportExtended getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	@Resource
	public void setDao(HibernateDaoSupportExtended dao) {
		this.dao = dao;
	}

}
