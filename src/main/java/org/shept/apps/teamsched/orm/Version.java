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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** 
 * @version $$Id: Version.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * @author Andi
 *
 */
@Entity
@Table(name="version")
public class Version implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5936519349355877808L;

	private Integer id;

	private Calendar dateTime;

	private String topic;

	private String version;

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
	 * @return the dateTime
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateTime", nullable = false)
	public Calendar getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return the topic
	 */
	@Column(name="topic", length=16, nullable=false)
	public String getTopic() {
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return the version
	 */
	@Column(name="version", length=16, nullable=false)
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
