/**
 * 
 */
package org.shept.apps.teamsched.web.controller.postprocessors;

import java.util.List;

import javax.annotation.Resource;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.Workgroup;
import org.shept.apps.teamsched.web.security.AuthenticationUtils;
import org.shept.org.springframework.web.bind.support.ComponentPostprocessor;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.ModelAndView;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class ReportsPostProcessor extends WebApplicationObjectSupport implements
		ComponentPostprocessor {
	
	private HibernateDaoSupport dao;

	public HibernateDaoSupport getDao() {
		return dao;
	}

	@Resource
	public void setDao(HibernateDaoSupport dao) {
		this.dao = dao;
	}

/* (non-Javadoc)
		 * @see org.shept.org.springframework.web.bind.support.ComponentPostprocessor#postHandle(org.springframework.web.context.request.WebRequest, org.springframework.web.servlet.ModelAndView, java.lang.String)
		 */
		public void postHandle(WebRequest request, ModelAndView mv,
				String componentPath) {

			if (mv != null) {
				User usr = (User) AuthenticationUtils.getUser();
				usr = dao.getHibernateTemplate().load(User.class, usr.getId());

				Integer usrId = Integer.valueOf(usr.getIdentifier());
				List<Workgroup> workgroups = dao.getHibernateTemplate().
					findByNamedQueryAndNamedParam("qryWorkgroups4Report", "currentUserId", usrId);
				mv.addObject("workgroups", workgroups);
				
				List<User> users = dao.getHibernateTemplate().
				findByNamedQueryAndNamedParam("qryUsersInvited", "userhostId", usrId);
				mv.addObject("users", users);
				
				//TODO onload -  make this a better (=more generic) solution - but its difficult
				mv.addObject("onload", "responseInNewWindow()" );
			}
		}
		


}
