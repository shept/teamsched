/**
 * 
 */
package org.shept.apps.teamsched.web.controller.postprocessors;

import javax.annotation.Resource;

import org.shept.apps.teamsched.orm.dao.TimeDao;
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
public class LoginAdminPostProcessor extends WebApplicationObjectSupport
		implements ComponentPostprocessor {
	
	private HibernateDaoSupport dao;

	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.bind.support.ComponentPostprocessor#postHandle(org.springframework.web.context.request.WebRequest, org.springframework.web.servlet.ModelAndView, java.lang.String)
	 */
	public void postHandle(WebRequest request, ModelAndView modelAndView,
			String componentPath) {
		modelAndView.addObject("userAgents", ((TimeDao) dao).getHibernateTemplateExtended().findByNamedQuery("qryAllUserAgents"));
	}

	/**
	 * @return the dao
	 */
	public HibernateDaoSupport getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	@Resource
	public void setDao(HibernateDaoSupport dao) {
		this.dao = dao;
	}

}
