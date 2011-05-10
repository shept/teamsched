/**
 * 
 */
package org.shept.apps.teamsched.web.controller.filters;

import java.io.Serializable;
import java.util.Calendar;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.shept.apps.teamsched.orm.LoginLog;
import org.shept.persistence.ModelCreation;
import org.shept.persistence.provider.hibernate.HibernateCriteriaDefinition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.SortDefinition;
import org.springframework.util.StringUtils;

/** 
 * @version $$Id: LoginHistoryFilter.java,v 1.1 2009/11/27 18:53:10 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class LoginHistoryFilter
	implements HibernateCriteriaDefinition, Serializable
	 {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String remoteAddr;
	private Calendar dateFrom;
	private Calendar dateTill;
	private Boolean bsuccess;
	private Boolean blogoutMissing = false;
	private Integer userAgentId;

	/* (non-Javadoc)
	 * @see org.shept.provider.hibernate.HibernateCriteriaProvider#getCriteria(java.lang.Object)
	 */
	public DetachedCriteria getCriteria(SortDefinition sortDefinition) {
		DetachedCriteria crit = DetachedCriteria.forClass(LoginLog.class);
		
		if (StringUtils.hasText(getUserName())) {
			crit.add(Restrictions.eq("userName", getUserName()));
		}
		
		if (StringUtils.hasText(getRemoteAddr())) {
			crit.add(Restrictions.eq("remoteAddr", getRemoteAddr()));
		}
		
		if (getBsuccess() != null) {
			crit.add(Restrictions.eq("bsuccess", getBsuccess()));
		}
		
		if (getBlogoutMissing()) {
			crit.add(Restrictions.isNull("dateLogout"));
		}
		
		if (getDateFrom() != null) {
			crit.add(Restrictions.gt("dateLogin", getDateFrom()));
		}
		
		if (getDateTill() != null) {
			Calendar till = Calendar.getInstance();
			till.setTime(getDateTill().getTime());
		    till.add(Calendar.DAY_OF_YEAR, 1);
			crit.add(Restrictions.lt("dateLogin", getDateTill()));
		}
		
		if (getUserAgentId() != null) {
			crit.add(Restrictions.eq("agentId", getUserAgentId()));
		}
		
		// set the default sorting if no sorting is specified
		if (sortDefinition != null &&  ! StringUtils.hasText(sortDefinition.getProperty())) {
			BeanUtils.copyProperties(getDefaultSort(), sortDefinition);
		}
		
		// set sort criteria from FormFilter
		if (null != sortDefinition && StringUtils.hasText(sortDefinition.getProperty())) {
			if (sortDefinition.isAscending())
				crit.addOrder(Order.asc(sortDefinition.getProperty()));
			else
				crit.addOrder(Order.desc(sortDefinition.getProperty()));
		}

		return crit;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the remoteAddr
	 */
	public String getRemoteAddr() {
		return remoteAddr;
	}

	/**
	 * @param remoteAddr the remoteAddr to set
	 */
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	/**
	 * @return the dateFrom
	 */
	public Calendar getDateFrom() {
		return dateFrom;
	}

	/**
	 * @param dateFrom the dateFrom to set
	 */
	public void setDateFrom(Calendar dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * @return the dateTill
	 */
	public Calendar getDateTill() {
		return dateTill;
	}

	/**
	 * @param dateTill the dateTill to set
	 */
	public void setDateTill(Calendar dateTill) {
		this.dateTill = dateTill;
	}

	/**
	 * @return the bsuccess
	 */
	public Boolean getBsuccess() {
		return bsuccess;
	}

	/**
	 * @param bsuccess the bsuccess to set
	 */
	public void setBsuccess(Boolean bsuccess) {
		this.bsuccess = bsuccess;
	}

	public Boolean getBlogoutMissing() {
		return blogoutMissing;
	}

	public void setBlogoutMissing(Boolean blogoutMissing) {
		this.blogoutMissing = blogoutMissing;
	}

	public Integer getUserAgentId() {
		return userAgentId;
	}

	public void setUserAgentId(Integer userAgentId) {
		this.userAgentId = userAgentId;
	}
	
	private MutableSortDefinition getDefaultSort() {
		MutableSortDefinition md = new MutableSortDefinition();
		md.setProperty("dateLogin");
		md.setAscending(false);
		return md;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((blogoutMissing == null) ? 0 : blogoutMissing.hashCode());
		result = prime * result
				+ ((bsuccess == null) ? 0 : bsuccess.hashCode());
		result = prime * result
				+ ((dateFrom == null) ? 0 : dateFrom.hashCode());
		result = prime * result
				+ ((dateTill == null) ? 0 : dateTill.hashCode());
		result = prime * result
				+ ((remoteAddr == null) ? 0 : remoteAddr.hashCode());
		result = prime * result
				+ ((userAgentId == null) ? 0 : userAgentId.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LoginHistoryFilter other = (LoginHistoryFilter) obj;
		if (blogoutMissing == null) {
			if (other.blogoutMissing != null)
				return false;
		} else if (!blogoutMissing.equals(other.blogoutMissing))
			return false;
		if (bsuccess == null) {
			if (other.bsuccess != null)
				return false;
		} else if (!bsuccess.equals(other.bsuccess))
			return false;
		if (dateFrom == null) {
			if (other.dateFrom != null)
				return false;
		} else if (!dateFrom.equals(other.dateFrom))
			return false;
		if (dateTill == null) {
			if (other.dateTill != null)
				return false;
		} else if (!dateTill.equals(other.dateTill))
			return false;
		if (remoteAddr == null) {
			if (other.remoteAddr != null)
				return false;
		} else if (!remoteAddr.equals(other.remoteAddr))
			return false;
		if (userAgentId == null) {
			if (other.userAgentId != null)
				return false;
		} else if (!userAgentId.equals(other.userAgentId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	/**
	 * Filter cannot be used to create new instances - return null
	 */
	public ModelCreation getNewModelTemplate() {
		return null;
	}

}
