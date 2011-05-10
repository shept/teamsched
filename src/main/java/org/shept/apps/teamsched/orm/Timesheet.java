package org.shept.apps.teamsched.orm;

// Generated 16.01.2009 21:43:54 by Hibernate Tools 3.2.2.GA

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.shept.persistence.ModelCreation;
import org.shept.util.DateUtils;

/** 
 * @version $$Id: Timesheet.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * Initial version generated  by hbm2java
 * 
 *
 * @author Andi
 *
 */
@Entity
@Table(name = "timesheet", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "datetimefrom" }))
public class Timesheet implements Serializable, Comparable<Timesheet>, ModelCreation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5083141776518296251L;
	private Long id;
	private Calendar version;
	private Calendar datecreated = Calendar.getInstance();
	private User user;
	private Integer issueId;
	private Issue issue;
	private Calendar datetimefrom;
	private Calendar datetimetill;
	private String comment;
	private Integer fdel = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Version
	@Column(name = "version", nullable = false)
	public Calendar getVersion() {
		return this.version;
	}

	public void setVersion(Calendar version) {
		this.version = version;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", nullable=false)
	public Calendar getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Calendar datecreated) {
		this.datecreated = datecreated;
	}

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@ForeignKey(name="fk_timesheet_user")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the issueId
	 */
	@Column(name = "issue_id", nullable = true)
	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	@ManyToOne
	@JoinColumn(name = "issue_id", nullable = true, insertable = false, updatable = false)
	@ForeignKey(name="fk_timesheet_issue")
	public Issue getIssue() {
		return this.issue;
	}

	public void setIssue(Issue issueByIssueId) {
		this.issue = issueByIssueId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datetimefrom", nullable = false)
	public Calendar getDatetimefrom() {
		return this.datetimefrom;
	}

	public void setDatetimefrom(Calendar datetimefrom) {
		this.datetimefrom = datetimefrom;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datetimetill")
	public Calendar getDatetimetill() {
		return this.datetimetill;
	}

	public void setDatetimetill(Calendar datetimetill) {
		this.datetimetill = datetimetill;
	}

	@Column(name = "comment")
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/*
	 * This method is to fulfill beans requirements to have the same getter /
	 * setter names
	 */
	@Transient
	public Date getTimefrom() {
		Calendar rv = getDatetimefrom();
		return (rv==null ? null : rv.getTime());
	}

	/*
	 * Here we merge the days existing Date part with the newly set Time part
	 * The dateTimeFrom instance Variable must already be set
	 */
	public void setTimefrom(Date d)  {
			Calendar cal = Calendar.getInstance(getDatetimefrom().getTimeZone());
			cal.setTime(d);
			setDatetimefrom(DateUtils.merge(getDatetimefrom(), cal));
	}

	/*
	 * This method is to fulfill beans requirements to have the same getter /
	 * setter names
	 */
	@Transient
	public Date getTimetill() {
		Calendar rv = getDatetimetill();
		return (rv == null ? null : rv.getTime());
	}

	/*
	 * Here we merge the days existing Date part with the newly set Time part
	 * The dateTimeFrom instance Variable must already be set 
	 */
	public void setTimetill(Date d) {
			if (d == null)
				return;
			Calendar cal = Calendar.getInstance(getDatetimefrom().getTimeZone());
			cal.setTime(d);
			setDatetimetill(DateUtils.merge(
					(getDatetimetill() != null ? getDatetimetill()
							: getDatetimefrom()), cal));
	}

	public int compareTo(Timesheet ts) {
		return this.getDatetimefrom().compareTo(ts.getDatetimefrom());
	}

	@Transient
	public boolean isTransient() {
		return getId() == null;
	}

	public boolean isCreationAllowed(Object editedObject) {
		Timesheet ts = (Timesheet) editedObject;
		return ts.getDatetimetill() != null && ts.getIssueId() != null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((datetimefrom == null) ? 0 : datetimefrom.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * check the business key, there should also be a unique constraint on these fields !
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Timesheet other = (Timesheet) obj;
		if (datetimefrom == null) {
			if (other.datetimefrom != null)
				return false;
		} else if (!datetimefrom.equals(other.datetimefrom))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	/**
	 * @return the fdel
	 */
	@Column(name="fdel" , nullable = false)
	public Integer getFdel() {
		return fdel;
	}

	/**
	 * @param fdel the fdel to set
	 */
	public void setFdel(Integer fdel) {
		this.fdel = fdel;
	}

}
