/**
 * 
 */
package org.shept.apps.teamsched.web.controller.filters;

import java.io.Serializable;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.Workgroup;
import org.shept.apps.teamsched.web.security.AuthenticationUtils;
import org.shept.beans.support.QueryDefinition;
import org.shept.persistence.ModelCreation;

/**
 * @author lucid64
 *
 */
public class UsersAndWorkgroupsFilter implements QueryDefinition, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	
	private Workgroup workgroup;

	/* (non-Javadoc)
	 * @see org.shept.beans.support.FilterDefinition#getNewModelTemplate()
	 */
	public ModelCreation getNewModelTemplate() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.shept.beans.support.QueryDefinition#getQuery()
	 */
	public String getQuery() {
		if (workgroup != null ) {
			return "qryUserAndWorkgroupsByHostAndWorkgroup";
		}
		else if (user != null) {
			return "qryUserAndWorkgroupsByHostAndUser";
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.shept.beans.support.QueryDefinition#getParamNames()
	 */
	public String[] getParamNames() {
		if (workgroup != null ) {
			return new String[]{"userhostId", "wgId" };
		}
		else if (user != null) {
			return new String[]{"userhostId", "userinvId" };
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.shept.beans.support.QueryDefinition#getValues()
	 */
	public Object[] getValues() {
		if (workgroup != null ) {
			return new Object[] {getHostUserId(), workgroup.getId()};
		}
		else if (user != null) {
			return new Object[] {getHostUserId(), user.getId()};
		}
		return null;
	}
	
	public void initialize (User user) {
		this.user = user;
	}
	
	public void initialize (Workgroup workgroup) {
		this.workgroup = workgroup;
	}
	
	private Integer getHostUserId() {
		User usr = ((User) AuthenticationUtils.getUser());
		if (usr == null) {
			return null;
		}
		return usr.getId();
	}
 
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result
				+ ((workgroup == null) ? 0 : workgroup.hashCode());
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
		UsersAndWorkgroupsFilter other = (UsersAndWorkgroupsFilter) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (workgroup == null) {
			if (other.workgroup != null)
				return false;
		} else if (!workgroup.equals(other.workgroup))
			return false;
		return true;
	}
	
	

}
