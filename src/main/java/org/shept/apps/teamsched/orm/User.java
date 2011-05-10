package org.shept.apps.teamsched.orm;

// Original version Generated 16.01.2009 21:43:54 by Hibernate Tools 3.2.2.GA

import java.io.Serializable;
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
import javax.persistence.JoinTable;
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
import org.shept.apps.teamsched.web.controller.commands.UserAdminEditMode;
import org.shept.apps.teamsched.web.controller.commands.UserCommand;
import org.shept.beans.support.ExampleDefinition;
import org.shept.org.springframework.beans.support.CommandSupplier;
import org.shept.persistence.ModelCreation;
import org.shept.persistence.ModelDeletion;
import org.shept.util.BeanUtilsExtended;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

/** 
 * @version $$Id: User.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * Initial version generated  by hbm2java 
 * 
 *
 * @author Andi
 *
 */
@Entity
@Table(name = "user1", uniqueConstraints={
		@UniqueConstraint(columnNames={"username", "fdel"}), 
		@UniqueConstraint(columnNames={"email", "fdel"}) })
public class User implements Serializable, UserDetails, ModelCreation, ModelDeletion, ExampleDefinition, CommandSupplier {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8649115750193349552L;
	private Integer id;
	private Calendar version;
	private Calendar datecreated = Calendar.getInstance();
	private String username;
	private String userpassword;
	private String newpassword;
	private String name;
	private String firstname;
	private String email;
	private String newemail;
	private Integer roleId = 0;
	private String company;
	private Calendar birthday;
	private Integer birthyear;
	private String countryIso ="DEU";
	private String zip;
	private String city;
	private Integer fdel = 0;
	private Boolean bactive = true;
	private Boolean bsticky = true;
	private Calendar datTerms;
	private User confirmingUser;
	private User parentUser;
	private Set<Timesheet> timesheets = new HashSet<Timesheet>(0);
	private List<Workgroup> workgroups = new ArrayList<Workgroup>(0);
	private List<Workgroup> ownedWorkgroups = new ArrayList<Workgroup>(0);
	private List<User> usersInvited = new ArrayList<User>(0);
	private List<LoginLog> loginLogs = new ArrayList<LoginLog>(0);

	/** 
	 * Unfortunately this method cannot be static as static interfaces are not supported by java
	* @return a default user
	*
	 */
	public static User getInstance() {
		User user = new User();
		return user;
	}
	
	public static User getInstanceAdmin() {
		User usr = getInstance();
		usr.setRoleId(1);		// Administrator
		usr.setUserpassword("admin");
		usr.setUsername("admin");
		usr.setName("Admin");
		return usr;
	}

	/**
	 * return a default User instance to be used as a template when creating a new user
	 */
	public User getInstance(Object requestOriginator) {
		User usr = getInstance();
		return usr;
	}
	
	/**
	 * Instatiate new users by {@link #getInstance()}
	 * or existing users by JPA / hibernate
	 */
	public User() {
	}

	/**
	 * Return a default instance for searching (filtering) user.
	 * The instance must be populated with default entities.
	 * Fields with a Null value will not be used for filtering
	 * 
	 * @param
	 * @return
	 *
	 * @return
	 */
	public void initializeFilter() {
		setBactive(true);
		setFdel(null);
		setBsticky(null);
		setCountryIso("DEU");
		setDatecreated(null);
		setRoleId(null);
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

	@Column(name = "username", length = 45)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		if (username != null) {
			this.username = username.toLowerCase();
		}
		else {
			this.username = username;
		}
	}

	@Column(name = "userpassword", length = 45)
	public String getUserpassword() {
		return this.userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	@Column(name = "newpassword", length = 45)
	public String getNewpassword() {
		return this.newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	@Column(name = "name", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "firstname", length = 45)
	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		if (email != null) {
			this.email = email.toLowerCase();
		}
		else {
			this.email = email;
			}
	}

	@Column(name = "newemail")
	public String getNewemail() {
		return newemail;
	}

	public void setNewemail(String newemail) {
		this.newemail = newemail;
	}

	/**
	 * @return the email with hidden characters
	 */
	@Transient
	public String getEmailHidden() {
		if (email == null) {
			return null;
		}
		int idx = email.indexOf("@");
		return email.substring(0, idx + 1) + "...";
	}

	/**
	 * @param don't set the hidden email
	 */
	public void setEmailHidden(String email) {
	}

	@Column(name = "role_id")
	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Column(name = "company", length = 45)
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "birthday")
	public Calendar getBirthday() {
		return birthday;
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}

	@Column(name = "birthyear")
	public Integer getBirthyear() {
		return birthyear;
	}

	public void setBirthyear(Integer birthyear) {
		this.birthyear = birthyear;
	}

	@Transient
	public Long getAge() {
		final Long yearInMsecs = 1000L * 3600 * 24 * 365;
		if (getBirthday() == null) return null;
		Calendar cal = Calendar.getInstance();
		Long period = cal.getTime().getTime() - getBirthday().getTime().getTime();
		return period / yearInMsecs;
	}

	@Column(name = "country_iso", length = 3)
	public String getCountryIso() {
		return countryIso;
	}

	public void setCountryIso(String countryIso) {
		this.countryIso = countryIso;
	}

	@Column(name = "zip", length = 12)
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(name = "city", length = 45)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "bactive")
	public Boolean getBactive() {
		return this.bactive;
	}

	public void setBactive(Boolean bactive) {
		this.bactive = bactive;
	}

	
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.REFRESH)
	@JoinTable(
			name="userworkgroup", 
			joinColumns = {@JoinColumn(name="user_id", insertable=false, updatable=false, nullable=false )},
			inverseJoinColumns = {@JoinColumn(name="workgroup_id" , insertable=false, updatable=false, nullable=false) })
	@OrderBy (value="name")
	public List<Workgroup> getWorkgroups() {
		return this.workgroups;
	}

	public void setWorkgroups(List<Workgroup> workgroups) {
		this.workgroups = workgroups;
	}

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="userinvitation", 
			joinColumns = {@JoinColumn(name="userhost_id", insertable=false, updatable= false, nullable=false)},
			inverseJoinColumns = {@JoinColumn(name="userinvited_id", insertable=false, updatable=false, nullable=false) })
	public List<User> getUsersInvited() {
		return this.usersInvited;
	}

	public void setUsersInvited(List<User> usersInvited) {
		this.usersInvited = usersInvited;
	}

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Timesheet> getTimesheets() {
		return this.timesheets;
	}

	public void setTimesheets(Set<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "user")
	@OrderBy (value="dateLogin desc" )
	public List<LoginLog> getLoginLogs() {
		return this.loginLogs;
	}

	public void setLoginLogs(List<LoginLog> loginLogs) {
		this.loginLogs = loginLogs;
	}

	
	/**
	 * @return the ownedWorkgroups
	 */
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, targetEntity=Workgroup.class )
	@JoinColumn(name="owner_id" )
	@OrderBy (value="name")
	@Where (clause="fdel = 0")
	public List<Workgroup> getOwnedWorkgroups() {
		return ownedWorkgroups;
	}

	/**
	 * @param ownedWorkgroups the ownedWorkgroups to set
	 */
	public void setOwnedWorkgroups(List<Workgroup> ownedWorkgroups) {
		this.ownedWorkgroups = ownedWorkgroups;
	} 

	
	@Transient
	public String getDisplayName() {
		return getUsername();
	}
	
	@Transient
	public String getIdentifier() {
		return String.valueOf(getId());
	}
	
	@Transient
	public Boolean getBdisabled() {
		return ! bactive || (0 != fdel);
	}

	@Transient
	public List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		if (null == getDatTerms()) {
			auths.add(new GrantedAuthorityImpl("ROLE_CONFIRM_TERMS"));
		} else {
			if (getRoleId() == 1) {
				auths.add(new GrantedAuthorityImpl("ROLE_ADMINISTRATOR"));
			} else {
				auths.add(new GrantedAuthorityImpl("ROLE_USER"));
			}			
		}
		return auths;
	}

	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Transient
	public boolean isEnabled() {
		return true;
	}

	@Transient
	public String getPassword() {
		return getUserpassword();
	}

	
	@Transient
	public boolean isTransient() {
		return getId() == null;
	}

	/**
	 * For equals/hash implementation check the business key !
	 * There should also be a unique constraint on these fields !
	 */
	
	@Transient
	public boolean isCreationAllowed(Object editedObject) {
		User edit = (User) editedObject;
		int cnt = 0;
		if (! StringUtils.hasText(edit.getUsername())) return false;
		if (StringUtils.hasText(username)) {
			cnt ++;
			if ( ! username.equalsIgnoreCase(edit.getUsername())) return false;
		}

		if (! StringUtils.hasText(edit.getName())) return false;
		if (StringUtils.hasText(name)) {
			cnt ++;
			if ( ! name.equals(edit.getName())) return false;
		}

		if (! StringUtils.hasText(edit.getFirstname())) return false;
		if (StringUtils.hasText(firstname)) {
			cnt ++;
			if ( ! firstname.equals(edit.getFirstname())) return false;
		}

		if (! StringUtils.hasText(edit.getEmail())) return false;
		if (StringUtils.hasText(email)) {
			cnt ++;
			if ( ! email.equalsIgnoreCase(edit.getEmail())) return false;
		}
		
		if (cnt < 4) return true;		// less than 4 fields were predefined by filter, but all 4 are now filled ...
		return StringUtils.hasText(edit.getCompany()) || StringUtils.hasText(edit.getCity()) || StringUtils.hasText(edit.getZip());
	}

	@Transient
	public ModelCreation getNewModelTemplate() {
		User newUser = User.getInstance();
		BeanUtilsExtended.mergeProperties(this, newUser);
		return newUser;
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

	@Transient
	public Object getCommand() {
		UserCommand wrapper = new UserCommand();
		wrapper.setEditMode(isTransient() ? UserAdminEditMode.USER_CREATE : UserAdminEditMode.USER_EDIT);
		wrapper.setUser(this);
		return wrapper;
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

	/**
	 * @return the bsticky
	 */
	@Column(name="bsticky")
	public Boolean getBsticky() {
		return bsticky;
	}

	/**
	 * @param bsticky the bsticky to set
	 */
	public void setBsticky(Boolean bsticky) {
		this.bsticky = bsticky;
	}

	/**
	 * @return the parentUser
	 */
	@ManyToOne
	@JoinColumn(name="parent_user_id", nullable=true)
	@ForeignKey(name="fk_user_parentUser")
	public User getParentUser() {
		return parentUser;
	}

	/**
	 * @param parentUser the parentUser to set
	 */
	public void setParentUser(User parentUser) {
		this.parentUser = parentUser;
	}

	/**
	 * @return the confirmingUser
	 */
	@ManyToOne
	@JoinColumn(name="confirming_user_id", nullable=true)
	@ForeignKey(name="fk_user_confirmingUser")
	public User getConfirmingUser() {
		return confirmingUser;
	}

	/**
	 * @param confirmingUser the confirmingUser to set
	 */
	public void setConfirmingUser(User confirmingUser) {
		this.confirmingUser = confirmingUser;
	}

	/**
	 * @return the datTerms
	 */
	public Calendar getDatTerms() {
		return datTerms;
	}

	/**
	 * @param datTerms the datTerms to set
	 */
	public void setDatTerms(Calendar datTerms) {
		this.datTerms = datTerms;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fdel == null) ? 0 : fdel.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fdel == null) {
			if (other.fdel != null)
				return false;
		} else if (!fdel.equals(other.fdel))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
