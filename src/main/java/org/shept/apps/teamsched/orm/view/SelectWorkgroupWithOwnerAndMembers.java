/**
 * 
 */
package org.shept.apps.teamsched.orm.view;

import java.io.Serializable;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.Workgroup;
import org.shept.org.springframework.beans.support.ModelSupplier;
import org.shept.persistence.ModelCreation;
import org.springframework.beans.BeanUtils;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class SelectWorkgroupWithOwnerAndMembers implements Serializable, ModelSupplier, ModelCreation, Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Workgroup workgroup;
	
	private User userOwner;
	
	private User userMember;

	/**
	 * 
	 */
	public SelectWorkgroupWithOwnerAndMembers() {
		super();
	}

	/**
	 * @param member
	 * @param workgroup
	 * @param owner
	 */
	public SelectWorkgroupWithOwnerAndMembers(User userMember,
			Workgroup workgroup, User userOwner) {
		super();
		this.userMember = userMember;
		this.workgroup = workgroup;
		this.userOwner = userOwner;
	}

	/**
	 * @return the workgroup
	 */
	public Workgroup getWorkgroup() {
		return workgroup;
	}

	/**
	 * @return the userOwner
	 */
	public User getUserOwner() {
		return userOwner;
	}

	/**
	 * @return the userMember
	 */
	public User getUserMember() {
		return userMember;
	}

	public Object getModel() {
		return getWorkgroup();
	}

	public boolean isTransient() {
		return getWorkgroup().isTransient();
	}

	public boolean isCreationAllowed(Object editedObject) {
		if (getWorkgroup() == null) {
			return false;
		} else {
			return getWorkgroup().isCreationAllowed(editedObject);
		}
	}

	/**
	 * @param workgroup the workgroup to set
	 */
	public void setWorkgroup(Workgroup workgroup) {
		this.workgroup = workgroup;
	}

	/**
	 * @param userOwner the userOwner to set
	 */
	public void setUserOwner(User userOwner) {
		this.userOwner = userOwner;
	}

	/**
	 * @param userMember the userMember to set
	 */
	public void setUserMember(User userMember) {
		this.userMember = userMember;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		SelectWorkgroupWithOwnerAndMembers copy = new SelectWorkgroupWithOwnerAndMembers();
		copy.userMember = BeanUtils.instantiate(userMember.getClass());
		BeanUtils.copyProperties(userMember, copy.userMember);
		copy.workgroup = BeanUtils.instantiate(workgroup.getClass());
		BeanUtils.copyProperties(workgroup, copy.workgroup);
		copy.userOwner = BeanUtils.instantiate(userOwner.getClass());
		BeanUtils.copyProperties(userOwner, copy.userOwner);
		return copy;
	}


}
