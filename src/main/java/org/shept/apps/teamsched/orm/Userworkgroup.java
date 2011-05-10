package org.shept.apps.teamsched.orm;

// Generated 16.01.2009 21:43:54 by Hibernate Tools 3.2.2.GA

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

/** 
 * @version $$Id: Userworkgroup.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * @author Andi
 *
 */
@Entity
@Table(name = "userworkgroup")
public class Userworkgroup implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4998373408416989429L;
	private UserworkgroupId id;
	private int user_id;
	private User user;
	private int workgroup_id;
	private Workgroup workgroup;

	public Userworkgroup() {
	}

	public Userworkgroup( User user, Workgroup workgroup) {
		this.id = new UserworkgroupId(user.getId(), workgroup.getId());
		this.user = user;
		this.user_id = user.getId();
		this.workgroup = workgroup;
		this.workgroup_id = workgroup.getId();
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "workgroupId", column = @Column(name = "workgroup_id", nullable = false)),
			@AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false)) })
	public UserworkgroupId getId() {
		return this.id;
	}

	public void setId(UserworkgroupId id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
	@ForeignKey(name="fk_userWorkgroup_user")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name = "workgroup_id", nullable = false, insertable = false, updatable = false)
	@ForeignKey(name="fk_userWorkgroup_workgroup")
	public Workgroup getWorkgroup() {
		return this.workgroup;
	}

	public void setWorkgroup(Workgroup workgroup) {
		this.workgroup = workgroup;
	}

	/**
	 * @return the user_id
	 */
	@Column(name="user_id", insertable=false, updatable=false)
	public int getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the workgroup_id
	 */
	@Column(name="workgroup_id", insertable=false, updatable=false)
	public int getWorkgroup_id() {
		return workgroup_id;
	}

	/**
	 * @param workgroup_id the workgroup_id to set
	 */
	public void setWorkgroup_id(int workgroup_id) {
		this.workgroup_id = workgroup_id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return id.equals(obj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	


}
