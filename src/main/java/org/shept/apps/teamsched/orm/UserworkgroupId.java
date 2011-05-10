package org.shept.apps.teamsched.orm;

// Generated 16.01.2009 21:43:54 by Hibernate Tools 3.2.2.GA

import javax.persistence.Column;
import javax.persistence.Embeddable;

/** 
 * @version $$Id: UserworkgroupId.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * Initial version generated  by hbm2java
 * 
 *
 * @author Andi
 *
 */
@Embeddable
public class UserworkgroupId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8770303341787258105L;
	private int workgroupId;
	private int userId;

	public UserworkgroupId() {
	}

	public UserworkgroupId( int userId, int workgroupId) {
		this.workgroupId = workgroupId;
		this.userId = userId;
	}

	@Column(name = "workgroup_id", nullable = false)
	public int getWorkgroupId() {
		return this.workgroupId;
	}

	public void setWorkgroupId(int workgroupId) {
		this.workgroupId = workgroupId;
	}

	@Column(name = "user_id", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserworkgroupId))
			return false;
		UserworkgroupId castOther = (UserworkgroupId) other;

		return (this.getWorkgroupId() == castOther.getWorkgroupId())
				&& (this.getUserId() == castOther.getUserId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getWorkgroupId();
		result = 37 * result + this.getUserId();
		return result;
	}

}
