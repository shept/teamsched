/**
 * 
 */
package org.shept.apps.teamsched.web.controller.filters;

import java.io.Serializable;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.Workgroup;
import org.shept.apps.teamsched.orm.view.SelectWorkgroupWithOwnerAndMembers;
import org.shept.apps.teamsched.web.controller.commands.WorkgroupSelectMode;
import org.shept.apps.teamsched.web.security.AuthenticationUtils;
import org.shept.beans.support.QueryDefinition;
import org.shept.persistence.ModelCreation;

/**
 * @author lucid64
 *
 */
public class WorkgroupModeFilter implements QueryDefinition, Serializable {
	
	private WorkgroupSelectMode mode = WorkgroupSelectMode.GROUPS_MEMBER;
	
	private User user;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.shept.beans.support.QueryDefinitionImpl#getQuery()
	 */
	public String getQuery() {
		switch (mode) {
		case GROUPS_OWNED:
			return "qryWorkgroupsWithOwnerAndMembersByOwner";
		case GROUPS_INVITED:
			return "qryWorkgroupsWithOwnerAndMembersByMemberInvited";
		default:
			return "qryWorkgroupsWithOwnerAndMembersByMember";
		}
	}

	/* (non-Javadoc)
	 * @see org.shept.beans.support.QueryDefinitionImpl#getParamNames()
	 */
	public String[] getParamNames() {
		return new String[]{"userId"};
	}

	/* (non-Javadoc)
	 * @see org.shept.beans.support.QueryDefinitionImpl#getValues()
	 */
	public Object[] getValues() {
		return new Object[]{getUserId()};
	}

	/* (non-Javadoc)
	 * @see org.shept.beans.support.QueryDefinitionImpl#getNewModelTemplate()
	 */
	public ModelCreation getNewModelTemplate() {
		if (mode != WorkgroupSelectMode.GROUPS_OWNED || getUser() == null) {
			return null;
		}
		Workgroup wg = Workgroup.getInstance();
		wg.setName("???");
		wg.setOwnerId(getUserId());
		wg.setOwner(getUser());
		return new SelectWorkgroupWithOwnerAndMembers(getUser(), wg, getUser());
	}

	public void initialize(User user) {
		this.user = user;
	}
	
	private User getCurrentUser() {
		return (User) AuthenticationUtils.getUser();
	}

	/**
	 * @return the mode
	 */
	public WorkgroupSelectMode getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(WorkgroupSelectMode mode) {
		this.mode = mode;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		if (user != null) {
			return user;
		}
		return getCurrentUser();
	}

	private Integer getUserId() {
		if (getUser() != null) {
			return getUser().getId();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkgroupModeFilter other = (WorkgroupModeFilter) obj;
		if (mode != other.mode)
			return false;
		return true;
	}

}
