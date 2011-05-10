package org.shept.apps.teamsched.orm.dao;

import java.util.Calendar;
import java.util.List;

import org.shept.apps.teamsched.orm.Issue;
import org.shept.apps.teamsched.orm.Timesheet;
import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.Userworkgroup;
import org.shept.apps.teamsched.orm.view.SelectWorkgroupUserRow;
import org.shept.apps.teamsched.orm.view.SelectWorkgroupWithOwnerAndMembers;
import org.shept.org.springframework.orm.hibernate3.support.HibernateDaoSupportExtended;
import org.shept.util.DateUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;


/** 
 * $$Id: TimeDao.java,v 1.1 2009/11/27 18:53:53 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class TimeDao extends HibernateDaoSupportExtended  implements ApplicationContextAware {
	
	private MessageSourceAccessor messageSourceAccessor;
		
	protected void initDao() throws Exception {
		super.initDao();
		createAdminIfAbsent();
	}
	
	/**
	 * Create an admin if we do not have any user in the db
	*
	 */
	private void createAdminIfAbsent() {
		String qs = "select count(*) from User";
		Long cnt = (Long) getHibernateTemplate().find(qs).get(0);
		if (cnt == 0) {
			User usr = User.getInstanceAdmin();
			getHibernateTemplate().save(usr);
			getHibernateTemplate().flush();
		}
	}

	/*
	 * This should work fast and with any database
	 * For large tables there should be an index an User_Id and dateTimeFrom
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> findTimesheets4Date(Calendar d, Long usrId) {
		Calendar datFrom = DateUtils.datePart(d);
		Calendar datTill = DateUtils.datePart(d);
		datTill.add(Calendar.DAY_OF_MONTH, 1);
		return (List<Timesheet>)getHibernateTemplate().
			findByNamedQueryAndNamedParam("qryTimesheetPeriod",
				new String[] { "datFrom", "datTill", "usrId" },
				new Object[] { datFrom, datTill, usrId.intValue() });
	}

	
	// ConfigUserController
    @SuppressWarnings("unchecked")
	public List<User> getUsers(String name) throws Exception {
    	return  getHibernateTemplate().
    		findByNamedQueryAndNamedParam("qryUserByUsername", "name", name);
    };

    @SuppressWarnings("unchecked")
		public List<Issue> qryUserIssues(Integer userId) {
			return (List<Issue>) getHibernateTemplate().
				findByNamedQueryAndNamedParam("qryUserIssues", "userId", userId);
	};
	
	/**
	 * Referenced by jsp submission parameter
	 * 
	 * @param
	 * @return
	 *
	 * @param userWorkgroups
	 */
	public void saveUserWorkgroups(List<SelectWorkgroupUserRow> userWorkgroups) {
		for (SelectWorkgroupUserRow row : userWorkgroups) {
			Userworkgroup uwg = new Userworkgroup(row.getUser(), row.getWorkgroup());
			if (row.getSelection() && ! row.getSelected()) {
				getHibernateTemplate().persist(uwg);
				row.setSelected(true);
			}
			if (! row.getSelection() && row.getSelected()) {
				getHibernateTemplate().delete(uwg);
				row.setSelected(false);
			}
		}
	}
	
	/**
	 * Referenced by jsp submission parameter
	 * 
	 * @param
	 * @return
	 *
	 * @param workgroups
	 */
	public void saveWorkgroups(List<SelectWorkgroupWithOwnerAndMembers> workgroupsWithOwners) {
		for (SelectWorkgroupWithOwnerAndMembers row : workgroupsWithOwners) {
			if (row.getWorkgroup().getId() == null) {
				String defaultWorkgroupName = messageSourceAccessor.getMessage("new.workgroup");
				if ( ! defaultWorkgroupName.equals(row.getWorkgroup().getName())) {
					getHibernateTemplate().save(row.getWorkgroup());
					Userworkgroup uwg = new Userworkgroup(row.getUserOwner(), row.getWorkgroup());
					getHibernateTemplate().save(uwg);
					Issue defaultIssue = Issue.getInstance();
					defaultIssue.setLevel(0);
					defaultIssue.setWorkgroupId(row.getWorkgroup().getId());
					String newIssueName = messageSourceAccessor.getMessage("new.issueFor", 
							new String[]{ row.getWorkgroup().getName()} );
					defaultIssue.setName(newIssueName);
					getHibernateTemplate().save(defaultIssue);					
				}				
			} else {
				getHibernateTemplate().update(row.getWorkgroup());
			}
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		messageSourceAccessor = new MessageSourceAccessor(applicationContext);
	}
	
}
