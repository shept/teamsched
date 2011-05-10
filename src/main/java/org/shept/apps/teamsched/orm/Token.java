/**
 * 
 */
package org.shept.apps.teamsched.orm;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
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
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/** 
 * @version $$Id: Token.java,v 1.1 2009/11/27 18:53:15 oops.oops Exp $$
 *
 * @author Andi
 *
 */
@Entity
@Table(name = "token")

public class Token implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4400123361593409432L;
	private Integer id;
	private Calendar version;
	private String token;
	private User user;
	private Integer actionId;
	private String properties;
	private Calendar datecreated = Calendar.getInstance();
	private Calendar dateaccepted ;
	private Calendar daterejected ;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Version
	@Column(name = "version", nullable = false)
	public Calendar getVersion() {
		return version;
	}

	public void setVersion(Calendar version) {
		this.version = version;
	}

	@Column(name = "token", length = 127, nullable=false, updatable=false)
	@Index(name="tokenIndex", columnNames={"token"})
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@ManyToOne(cascade={CascadeType.REFRESH, CascadeType.PERSIST})
	@JoinColumn(name = "user_id", nullable = true, updatable=false)
	@ForeignKey(name="fk_token_user")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name="action_id", nullable=false)
	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	@Column (name="properties", length=255)
	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", nullable=false)
	public Calendar getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Calendar datecreated) {
		this.datecreated = datecreated;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateaccepted")
	public Calendar getDateaccepted() {
		return dateaccepted;
	}

	public void setDateaccepted(Calendar dateaccepted) {
		this.dateaccepted = dateaccepted;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "daterejected")
	public Calendar getDaterejected() {
		return daterejected;
	}

	public void setDaterejected(Calendar daterejected) {
		this.daterejected = daterejected;
	}


}
