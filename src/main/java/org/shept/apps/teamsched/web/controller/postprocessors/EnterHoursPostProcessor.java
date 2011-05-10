/**
 * 
 */
package org.shept.apps.teamsched.web.controller.postprocessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.shept.apps.teamsched.orm.Issue;
import org.shept.apps.teamsched.orm.Timesheet;
import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.Workgroup;
import org.shept.apps.teamsched.orm.dao.TimeDao;
import org.shept.apps.teamsched.util.DomMenuUtils;
import org.shept.apps.teamsched.util.DomUtils;
import org.shept.apps.teamsched.util.JavaScriptUtils;
import org.shept.apps.teamsched.web.security.AuthenticationUtils;
import org.shept.org.springframework.beans.support.FilteredListHolder;
import org.shept.org.springframework.web.bind.support.ComponentPostprocessor;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentUtils;
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
public class EnterHoursPostProcessor extends WebApplicationObjectSupport implements ComponentPostprocessor {
	
	private HibernateDaoSupport dao;

	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.bind.support.ComponentPostprocessor#postHandle(org.springframework.web.context.request.WebRequest, org.shept.org.springframework.web.bind.support.ComponentDataBinder, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public void postHandle(WebRequest request, ModelAndView mv, String componentPath) {
		Object component = ComponentUtils.getComponent(mv, componentPath);
		List sheets = ((FilteredListHolder) component).getSource();
		
		TimeDao dao =  (TimeDao) getDao();
		User usr = (User) AuthenticationUtils.getUser();
		usr = dao.getHibernateTemplate().load(User.class, usr.getId());

		Integer usrId = Integer.valueOf(usr.getIdentifier());
		List issues = dao.qryUserIssues(usrId);

		List<Issue> issuesExcluded = dao.getHibernateTemplateExtended().
			findByNamedQueryAndNamedParam("qryIssuesExcluded", "userId", usrId);
		Map<Integer, String> issuesExcludedMap = new HashMap<Integer, String>();
		for (Issue issEx : issuesExcluded) {
				issuesExcludedMap.put(issEx.getId(), issEx.getName());
		}
		mv.addObject("issuesExcludedMap", issuesExcludedMap);

		List<Workgroup> workgroups = dao.getHibernateTemplateExtended().
			findByNamedQueryAndNamedParam("qryWorkgroups", "currentUserId", usrId);
		String dimArr = JavaScriptUtils.asJavaScript2DimArray(issueSelections(sheets, issues));
		mv.addObject("selections", dimArr);

		try {
			String wgIss = DomUtils.getWorkgroupWithIssuesXML(issues, workgroups );
			mv.addObject("xml", wgIss);
			
			String wgMenu = DomMenuUtils.getWorkgroupWithIssuesAsMenuXML(issues, workgroups);
			mv.addObject("xmlMenu", wgMenu);
		} catch (Exception e) {
			logger.error("An error occurred while building the com object with all the selection information for user " 
					+ ((User) AuthenticationUtils.getUser()).getUsername() , e);
		}
		mv.addObject("xmlNames", "[\"workgroup\", \"issue\", \"issue\", \"issue\"] ");
//		mv.addObject("xmlNames", JavaScriptUtils.asJavaScriptStringArray(
//			Arrays.asList(new String[] {"workgroup","issue", "issue", "issue"} )));

	}

	/*
	 * Return an ArrayList (rows) with an ArrayList (col)
	 * for each element in the Matrix
	 */
	@SuppressWarnings("unchecked")
	private List issueSelections(List<Timesheet> sheets, List issues) {
		// read the whole issues because we  need them all in the .jsp anyway
		Map issMap = issueMap(issues);
		
		List l = new ArrayList();
		Integer issId;
		for (Iterator iter = sheets.iterator(); iter.hasNext();) {
			Timesheet ts = (Timesheet) iter.next();
			List cols = new ArrayList();
			// do it in-memory
			// the other solution ts.getIssue() would involve n database lookups ?
			Issue iss = (Issue) issMap.get( ts.getIssueId() == null ? null : Long.valueOf(ts.getIssueId()));
			issId = null;
			while (iss != null) {
				issId = iss.getWorkgroupId();
				cols.add(iss.getId());
				iss = iss.getParentIssue();
			}
			if (issId != null) cols.add(issId);
			Collections.reverse(cols);
			l.add(cols);
		}
		return l;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private List issueSelection(Number wgId, Issue issue, List issues) {
		// read the whole issues because we  need them all in the .jsp anyway
		List<Number> cols = new ArrayList<Number>();
		Issue iss = issue;
		while (iss != null) {
			cols.add(iss.getId());
			iss = iss.getParentIssue();
		}
		if (wgId != null) cols.add(wgId);
		Collections.reverse(cols);
		return cols;
	}

	/*
	 * return all issues as a map with their id as key
	 */
	@SuppressWarnings("unchecked")
	private Map issueMap(List issues) {
		Map<Long, Issue> issMap = new HashMap<Long, Issue>();
		for (Iterator iter1 = issues.iterator(); iter1.hasNext();) {
			Issue is1 = (Issue) iter1.next();
			// here we avoid the lazy-init-errors by forcing a read
			logger.debug(is1.getId() + " " + is1.getName());
			issMap.put(Long.valueOf(is1.getId()), is1);
		}
		return issMap;
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
