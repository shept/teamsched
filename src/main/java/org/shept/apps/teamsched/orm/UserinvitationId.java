package org.shept.apps.teamsched.orm;

// Generated 16.01.2009 21:43:54 by Hibernate Tools 3.2.2.GA

import javax.persistence.Column;
import javax.persistence.Embeddable;

/** 
 * @version $$Id: UserinvitationId.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * Initial version generated  by hbm2java
 * 
 *
 * @author Andi
 *
 */
@Embeddable
public class UserinvitationId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4555113117243333349L;
	private int userhostId;
	private int userinvitedId;

	public UserinvitationId() {
	}

	public UserinvitationId(int userhostId, int userinvitedId) {
		this.userhostId = userhostId;
		this.userinvitedId = userinvitedId;
	}

	@Column(name = "userhost_id", nullable = false)
	public int getUserhostId() {
		return this.userhostId;
	}

	public void setUserhostId(int userhostId) {
		this.userhostId = userhostId;
	}

	@Column(name = "userinvited_id", nullable = false)
	public int getUserinvitedId() {
		return this.userinvitedId;
	}

	public void setUserinvitedId(int userinvitedId) {
		this.userinvitedId = userinvitedId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserinvitationId))
			return false;
		UserinvitationId castOther = (UserinvitationId) other;

		return (this.getUserhostId() == castOther.getUserhostId())
				&& (this.getUserinvitedId() == castOther.getUserinvitedId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserhostId();
		result = 37 * result + this.getUserinvitedId();
		return result;
	}

}
