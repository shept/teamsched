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
import javax.persistence.ManyToMany;
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
import org.hibernate.annotations.Where;
import org.shept.persistence.ModelCreation;
import org.shept.persistence.ModelDeletion;
import org.springframework.util.StringUtils;

/** 
 * @version $$Id: Workgroup.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * Initial version generated  by hbm2java
 * 
 *
 * @author Andi
 *
 */
@Entity
@Table(name = "workgroup", uniqueConstraints = @UniqueConstraint(columnNames={ "name", "owner_id", "fdel" }) )
public class Workgroup implements java.io.Serializable, ModelCreation, ModelDeletion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4288309178476706819L;
	private Integer id;
	private Calendar version;
	private Calendar datecreated = Calendar.getInstance();
	private String name;
	private int ownerId;
	private User owner;
	private Integer fdel = 0;
	private Set<Userworkgroup> userworkgroups = new HashSet<Userworkgroup>(0);
	private List<User> users = new ArrayList<User>(0);
	private List<Issue> rootIssues = new ArrayList<Issue>(0);
	

	public static Workgroup getInstance() {
		Workgroup workgroup = new Workgroup();
		return workgroup;
	}
	
	/**
	 * Instatiate new workgroups by {@link #getInstance()}
	 * or existing users by JPA / hibernate
	 */
	public Workgroup() {
	}

	/**
	 * Initialize workgroup from owning user
	 * @param user
	 */
	public void initialize(User user) {
		this.owner = user;
		this.ownerId = user.getId() == null ? 0 : user.getId();
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

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="owner_id")
	public int getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return the owner
	 */
	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false, insertable=false, updatable=false)
	@ForeignKey(name="fk_workgroup_user")
	public User getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workgroup")
	public Set<Userworkgroup> getUserworkgroups() {
		return this.userworkgroups;
	}

	public void setUserworkgroups(Set<Userworkgroup> userworkgroups) {
		this.userworkgroups = userworkgroups;
	}

	@ManyToMany(mappedBy="workgroups")
	@OrderBy(value="name")
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}


	@Transient
	public boolean isTransient() {
		return getId() == null;
	}

	/**
	 * compares the edited object(parameter) againts the template instance (this).
	 * Should only return true if the edited object is different from the template
	 * It must have been edited before getting saved
	 * 
	 * @param editedObject
	 * @return boolean
	 */
	public boolean isCreationAllowed(Object editedObject) {
		Workgroup wg = (Workgroup) editedObject;
		if ( ! StringUtils.hasText(wg.getName())) return false;
		if (StringUtils.hasText(name) && name.equalsIgnoreCase(wg.getName())) {
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
	 * @return the rootIssues
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workgroup")
	@Where(clause="level=0")
	public List<Issue> getRootIssues() {
		return rootIssues;
	}

	/**
	 * @param rootIssues the rootIssues to set
	 */
	public void setRootIssues(List<Issue> rootIssues) {
		this.rootIssues = rootIssues;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fdel == null) ? 0 : fdel.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ownerId;
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
		Workgroup other = (Workgroup) obj;
		if (fdel == null) {
			if (other.fdel != null)
				return false;
		} else if (!fdel.equals(other.fdel))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (ownerId != other.ownerId)
			return false;
		return true;
	}


}
