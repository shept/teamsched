/**
 * 
 */
package org.shept.apps.teamsched.web.controller.filters;

import java.io.Serializable;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.web.controller.commands.UserAdminSelectionMode;
import org.shept.apps.teamsched.web.security.AuthenticationUtils;
import org.shept.beans.support.QueryDefinition;
import org.shept.persistence.ModelCreation;

/**
 * @author lucid64
 *
 */
public class UserModeFilter implements QueryDefinition, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserAdminSelectionMode mode = UserAdminSelectionMode.USERS_HOSTED;

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
		switch (mode) {
		case USERS_INVITATIONS:
			return "qryUserByInvitations";
		case USERS_HOSTED:
			return "qryUserByHost";
		default:
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.shept.beans.support.QueryDefinition#getParamNames()
	 */
	public String[] getParamNames() {
		switch (mode) {
		case USERS_INVITATIONS:
			return new String[] {"userInvitedId"};
		case USERS_HOSTED:
			return new String[] {"userhostId"};
		default:
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.shept.beans.support.QueryDefinition#getValues()
	 */
	public Object[] getValues() {
		return new Object[] {getUser().getId()};
	}

	/**
	 * @return the mode
	 */
	public UserAdminSelectionMode getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(UserAdminSelectionMode mode) {
		this.mode = mode;
	}
	
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserModeFilter other = (UserModeFilter) obj;
		if (mode != other.mode)
			return false;
		return true;
	}

}
