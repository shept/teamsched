/**
 * 
 */
package org.shept.apps.teamsched.orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/** 
 * @version $$Id: UserAgent.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * @author Andi
 *
 */
@Entity
@Table(name = "userAgent", uniqueConstraints={
		@UniqueConstraint(columnNames={"userAgent"} ) } )
public class UserAgent implements Comparable<UserAgent>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5172226359693582432L;
	private Integer id;
	private String name;
	private String userAgent;
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(UserAgent userAgent) {
		return userAgent.getUserAgent().compareTo(getUserAgent());
	}

	/**
	 * @return the id
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the userAgent
	 */
	@Column(name="userAgent", nullable=false, length=2048)
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * @return the name
	 */
	@Column(name="name", length=64)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
