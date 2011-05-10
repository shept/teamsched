/**
 * 
 */
package org.shept.apps.teamsched.web.controller;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.Workgroup;
import org.shept.apps.teamsched.web.controller.commands.ReportsCommand;
import org.shept.apps.teamsched.web.security.AuthenticationUtils;
import org.shept.org.springframework.beans.support.CommandWrapper;
import org.shept.org.springframework.beans.support.DefaultCommandObject;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentUtils;
import org.shept.org.springframework.web.servlet.mvc.delegation.MultiActionController;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.web.servlet.ModelAndView;

/**
 * @version $$Id: $$
 * 
 * @author Andi
 * 
 */
public class ReportController extends MultiActionController {
	
	private HibernateDaoSupport dao;

	@Override
	protected Object buildCommandObject(HttpServletRequest request) {
		DefaultCommandObject pfo = new DefaultCommandObject();

		ReportsCommand cmd = new ReportsCommand();
		cmd.setDateFrom(Calendar.getInstance().getTime());
		cmd.setDateTill(Calendar.getInstance().getTime());

		CommandWrapper cw = new CommandWrapper();
		cw.setCommand(cmd);
		cw.setTagName("reports");
		pfo.getChildren().clear();
		pfo.getChildren().add(cw);
		ComponentUtils.applyConfiguration(cw, getWebApplicationContext());
		return pfo;
	}

	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.servlet.mvc.delegation.DelegatingController#postProcessModel(javax.servlet.http.HttpServletRequest, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	protected void postProcessModel(HttpServletRequest request,
			ModelAndView mv) throws Exception {
		super.postProcessModel(request, mv);
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
		}
	}

	/**
	 * @param dao the dao to set
	 */
	@Resource
	public void setDao(HibernateDaoSupport dao) {
		this.dao = dao;
	}

}
