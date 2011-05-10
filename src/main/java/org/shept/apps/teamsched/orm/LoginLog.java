/**
 * 
 */
package org.shept.apps.teamsched.orm;

import java.io.Serializable;
import java.util.Calendar;

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

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/** 
 * @version $$Id: LoginLog.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * @author Andi
 *
 */
@Entity
@Table(name="loginLog")
@org.hibernate.annotations.Table(appliesTo="loginLog", 
		indexes = { @Index(name="loginLogIndex", columnNames={"user_id", "dateLogin"} ) } )
public class LoginLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6089907996859421963L;
	private Long id;
	private String sessionId;
	private String userName;
	private String userPassword;
	private User user;
	private Boolean bsuccess;
	private Calendar dateLogin;
	private Calendar dateLogout;
	private String remoteAddr;
	private String remoteHost;
	private UserAgent agent;
	private Integer agentId;
	private UserScreen screen;

	/**
	 * @return the id
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the sessionId
	 */
	@Column(name = "sessionId", length = 255, unique=true, nullable=true)
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param userName the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the userName
	 */
	@Column(name = "username", length = 45)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userPassword
	 */
	@Column(name = "userpassword", length = 45)
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * @return the user
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
	@ForeignKey(name="fk_loginLog_user")
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the bsuccess
	 */
	@Column(name = "bsuccess", nullable=false)
	public Boolean getBsuccess() {
		return bsuccess;
	}
	
	/**
	 * @param success the bSuccess to set
	 */
	public void setBsuccess(Boolean success) {
		bsuccess = success;
	}

	/**
	 * @return the dateLogin
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateLogin", nullable = false)
	public Calendar getDateLogin() {
		return dateLogin;
	}

	/**
	 * @param dateLogin the dateLogin to set
	 */
	public void setDateLogin(Calendar dateLogin) {
		this.dateLogin = dateLogin;
	}

	/**
	 * @return the dateLogout
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateLogout", nullable = true)
	public Calendar getDateLogout() {
		return dateLogout;
	}

	/**
	 * @param dateLogout the dateLogout to set
	 */
	public void setDateLogout(Calendar dateLogout) {
		this.dateLogout = dateLogout;
	}

	/**
	 * @return the remoteAddr
	 */
	@Column(name="remoteAddress", length=1024)
	public String getRemoteAddr() {
		return remoteAddr;
	}

	/**
	 * @param remoteAddr the remoteAddr to set
	 */
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	/**
	 * @return the remoteHost
	 */
	@Column(name="remoteHost", length=1024)
	public String getRemoteHost() {
		return remoteHost;
	}

	/**
	 * @param remoteHost the remoteHost to set
	 */
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	/**
	 * @return the agent
	 */
	@ManyToOne
	@JoinColumn(name = "userAgent_id", nullable = true)
	@ForeignKey(name="fk_loginLog_userAgent")
	public UserAgent getAgent() {
		return agent;
	}

	/**
	 * @param agent the agent to set
	 */
	public void setAgent(UserAgent agent) {
		this.agent = agent;
	}

	@Column(name = "userAgent_id", nullable = true, insertable = false, updatable = false)
	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	/**
	 * @return the screen
	 */
	@ManyToOne
	@JoinColumn(name = "userScreen_id", nullable = true)
	@ForeignKey(name="fk_loginLog_userScreen")
	public UserScreen getScreen() {
		return screen;
	}

	/**
	 * @param screen the screen to set
	 */
	public void setScreen(UserScreen screen) {
		this.screen = screen;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		final LoginLog other = (LoginLog) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
