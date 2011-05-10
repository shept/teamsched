package org.shept.apps.teamsched.orm;

// Generated 16.01.2009 21:43:54 by Hibernate Tools 3.2.2.GA

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.shept.persistence.ModelCreation;
import org.shept.persistence.ModelDeletion;
import org.springframework.util.StringUtils;

/** 
 * @version $$Id: Issue.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * Initial version generated  by hbm2java
 * 
 * @author Andi
 *
 */
@Entity
@Table(name = "issue", uniqueConstraints = @UniqueConstraint(columnNames={ "level", "name", "workgroup_id", "fdel" }))
public class Issue implements java.io.Serializable, ModelCreation, ModelDeletion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9190101213699356688L;
	private Integer id;
	private Calendar version;
	private Calendar datecreated = Calendar.getInstance();
	private int level = 0;

	private Integer workaroundId;

	private String name;
	private int workgroupId;
	private Workgroup workgroup;
	private Issue parentIssue;
	private Integer issueId;
	private Integer fdel = 0;
	private Set<Timesheet> timesheetsForIssueId = new HashSet<Timesheet>(0);
	private List<Issue> childIssues = new ArrayList<Issue>(0);


	/** 
	* @return a default issue
	*
	 */
	public static Issue getInstance() {
		Issue issue = new Issue();
		return issue;
	}
	
	/**
	 * Instatiate new issues by {@link #getInstance()}
	 * or existing users by JPA / hibernate
	 */
	public Issue() {
	}
	
	public void initialize (Workgroup workgroup) {
		setWorkgroup(workgroup);
		setWorkgroupId(workgroup.getId());
	}
	
	public void initialize(Issue parentIssue) {
		setLevel(parentIssue.getLevel() + 1);
		setParentIssue(parentIssue);
		setIssueId(parentIssue.getId());
		setWorkgroup(parentIssue.getWorkgroup());
		setWorkgroupId(parentIssue.getWorkgroup().getId());
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
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

	@Column(name = "level", nullable = false)
	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="workgroup_id", nullable=false)
	public int getWorkgroupId() {
		return this.workgroupId;
	}

	public void setWorkgroupId(int workgroupId) {
		this.workgroupId = workgroupId;
	}

	@Column(name = "issue_id", nullable=true)
	public Integer getIssueId() {
		return this.issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "issue")
	public Set<Timesheet> getTimesheetsForIssueId() {
		return this.timesheetsForIssueId;
	}

	public void setTimesheetsForIssueId(Set<Timesheet> timesheetsForIssueId) {
		this.timesheetsForIssueId = timesheetsForIssueId;
	}

	/**
	 * @return the workgroup
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="workgroup_id", insertable=false, updatable=false, nullable=false)
	@ForeignKey(name="fk_issue_workgroup")
	public Workgroup getWorkgroup() {
		return workgroup;
	}

	/**
	 * @param workgroup the workgroup to set
	 */
	public void setWorkgroup(Workgroup workgroup) {
		this.workgroup = workgroup;
	}

	/**
	 * @return the parentIssue
	 */
	@ManyToOne
	@JoinColumn(name="issue_id", insertable=false, updatable=false, nullable=true)
	@ForeignKey(name="fk_issue_parentIssue")
	public Issue getParentIssue() {
		return parentIssue;
	}

	/**
	 * @param parentIssue the parentIssue to set
	 */
	public void setParentIssue(Issue parentIssue) {
		this.parentIssue = parentIssue;
	}

	/**
	 * @return the childIssues
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentIssue")
	@OrderBy(value="name")
	public List<Issue> getChildIssues() {
		return childIssues;
	}

	/**
	 * @param childIssues the childIssues to set
	 */
	public void setChildIssues(List<Issue> childIssues) {
		this.childIssues = childIssues;
	}

	/**
	 * @return the workaroundId
	 * because aof bug in Hibernae 3.4GA / javassist 3.4GA
	 */
	@Column(name = "id", nullable = false, updatable=false, insertable=false)
	public Integer getWorkaroundId() {
		return workaroundId;
	}

	/**
	 * @param testId the testId to set
	 */
	public void setWorkaroundId(Integer testId) {
		this.workaroundId = testId;
	}


	public String displayString() {
		String display = "";
		Issue parent = getParentIssue();
		if (parent == null) {
			display = getWorkgroup().getName();
		} else {
			display = parent.displayString();
		}
		if (display.length() > 0 ) {
			display = display + " -> ";
		}
		return display + getName();
	}

	@Transient
	public boolean isTransient() {
		return getId() == null;
	}

	public boolean isCreationAllowed(Object editedObject) {
		Issue iss = (Issue) editedObject;
		if ( ! StringUtils.hasText(iss.getName())) return false;
		if (StringUtils.hasText(name) && name.equalsIgnoreCase(iss.getName())) {
			return false;
		}
		return true;
	}
	
	public boolean setDeleted(boolean delete) {
		if (delete) {
			this.fdel = getId();			
		} else {
			this.fdel = null;
		}
		return (this.fdel != null);
	}

	@Transient
	public boolean isDeleted() {
		return (this.fdel != null && this.fdel != 0);
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fdel == null) ? 0 : fdel.hashCode());
		result = prime * result + level;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + workgroupId;
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
		Issue other = (Issue) obj;
		if (fdel == null) {
			if (other.fdel != null)
				return false;
		} else if (!fdel.equals(other.fdel))
			return false;
		if (level != other.level)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (workgroupId != other.workgroupId)
			return false;
		return true;
	}

}
