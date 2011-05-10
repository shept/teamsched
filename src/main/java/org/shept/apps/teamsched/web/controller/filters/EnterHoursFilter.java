/**
 * 
 */
package org.shept.apps.teamsched.web.controller.filters;

import java.io.Serializable;
import java.util.Calendar;

import org.shept.apps.teamsched.orm.Timesheet;
import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.web.security.AuthenticationUtils;
import org.shept.beans.support.QueryDefinition;
import org.shept.persistence.ModelCreation;
import org.shept.util.DateUtils;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class EnterHoursFilter implements QueryDefinition, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Calendar date = Calendar.getInstance();
	
	public String getQuery() {
		return "qryTimesheetPeriod";
	}

	public String[] getParamNames() {
		return new String[] { "datFrom", "datTill", "usrId" };
	}

	/* (non-Javadoc)
	 * @see org.shept.beans.support.QueryDefinitionImpl#getValues()
	 */
	public Object[] getValues() {
		Calendar datFrom = DateUtils.datePart(date);
		Calendar datTill = DateUtils.datePart(date);
		datTill.add(Calendar.DAY_OF_MONTH, 1);
		return new Object[] { datFrom, datTill, getUser().getId().intValue()};
	}

	/* (non-Javadoc)
	 * @see org.shept.beans.support.QueryDefinitionImpl#getNewModelTemplate()
	 */
	public ModelCreation getNewModelTemplate() {
		Timesheet ts = new Timesheet();
		ts.setDatetimefrom(DateUtils.merge(date, Calendar.getInstance()));
		ts.setUser(getUser());
		return ts;
	}

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return (User) AuthenticationUtils.getUser();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnterHoursFilter other = (EnterHoursFilter) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

	
}
