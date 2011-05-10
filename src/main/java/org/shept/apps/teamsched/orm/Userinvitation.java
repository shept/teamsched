package org.shept.apps.teamsched.orm;

// Generated 16.01.2009 21:43:54 by Hibernate Tools 3.2.2.GA

import java.util.Calendar;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

/** 
 * @version $$Id: Userinvitation.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * Initial version generated  by hbm2java
 * 
 *
 * @author Andi
 *
 */
@Entity
@Table(name = "userinvitation")
public class Userinvitation implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -296863440725405874L;
	private UserinvitationId id;
	private Calendar datetimeInvitation;
	private Calendar datetimeResponse;
	private Boolean baccepted;
	private User userInvited;
	private User userHost;

	public Userinvitation() {
	}

	public Userinvitation(UserinvitationId id) {
		this.id = id;
	}

	public Userinvitation(UserinvitationId id, Calendar datetimeInvitation,
			Calendar datetimeResponse, Boolean bAccepted) {
		this.id = id;
//		this.datetimeInvitation = datetimeInvitation;
		this.datetimeResponse = datetimeResponse;
		this.baccepted = bAccepted;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "userhostId", column = @Column(name = "userhost_id", nullable = false)),
			@AttributeOverride(name = "userinvitedId", column = @Column(name = "userinvited_id", nullable = false)) })
	public UserinvitationId getId() {
		return this.id;
	}

	public void setId(UserinvitationId id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datetime_invitation", nullable=true)
	public Calendar getDatetimeInvitation() {
		return this.datetimeInvitation;
	}

	public void setDatetimeInvitation(Calendar datetimeInvitation) {
		this.datetimeInvitation = datetimeInvitation;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datetime_response", nullable=true)
	public Calendar getDatetimeResponse() {
		return this.datetimeResponse;
	}

	public void setDatetimeResponse(Calendar datetimeResponse) {
		this.datetimeResponse = datetimeResponse;
	}

	@Column(name = "baccepted", nullable=true)
	public Boolean getBaccepted() {
		return this.baccepted;
	}

	public void setBaccepted(Boolean bAccepted) {
		this.baccepted = bAccepted;
	}
	
	@ManyToOne
	@JoinColumn(name = "userinvited_id", nullable = false, insertable = false, updatable = false)
	@ForeignKey(name="fk_userInvitation_userInvited")
	public User getUserInvited() {
		return this.userInvited;
	}

	public void setUserInvited(User userInvited) {
		this.userInvited = userInvited;
	}

	@ManyToOne
	@JoinColumn(name = "userhost_id", nullable = false, insertable = false, updatable = false)
	@ForeignKey(name="fk_userInvitation_userHost")
	public User getUserHost() {
		return this.userHost;
	}

	public void setUserHost(User userHost) {
		this.userHost = userHost;
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
