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
 * @version $$Id: UserScreen.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * @author Andi
 *
 */
@Entity
@Table(name = "userScreen", uniqueConstraints={
		@UniqueConstraint(columnNames={"screenResolution"} ) } )
		
public class UserScreen implements Comparable<UserScreen>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3462853837532932614L;
	private Integer id;
	private String name;
	private String screenResolution;
	private Integer sixeX;
	private Integer sixeY;
	
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
	 * @return the screenResolution
	 */
	@Column(name="screenResolution", length=2048)
	public String getScreenResolution() {
		return screenResolution;
	}

	/**
	 * @param screenResolution the screenResolution to set
	 */
	public void setScreenResolution(String screenResolution) {
		this.screenResolution = screenResolution;
	}

	/**
	 * @return the sixeX
	 */
	@Column(name="sixeX", nullable=true)
	public Integer getSixeX() {
		return sixeX;
	}

	/**
	 * @param sixeX the sixeX to set
	 */
	public void setSixeX(Integer sixeX) {
		this.sixeX = sixeX;
	}

	/**
	 * @return the sixeY
	 */
	@Column(name="sixeY", nullable=true)
	public Integer getSixeY() {
		return sixeY;
	}

	/**
	 * @param sixeY the sixeY to set
	 */
	public void setSixeY(Integer sixeY) {
		this.sixeY = sixeY;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(UserScreen userScreen) {
		return userScreen.getScreenResolution().compareTo(getScreenResolution());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((screenResolution == null) ? 0 : screenResolution.hashCode());
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
		final UserScreen other = (UserScreen) obj;
		if (screenResolution == null) {
			if (other.screenResolution != null)
				return false;
		} else if (!screenResolution.equals(other.screenResolution))
			return false;
		return true;
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
