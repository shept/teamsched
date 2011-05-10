package org.shept.apps.teamsched.web.controller.commands;



import java.io.Serializable;
import java.util.Date;


/** 
 * @version $$Id: ReportsCommand.java,v 1.1 2009/11/27 18:53:22 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class ReportsCommand  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date dateFrom;
	private Date dateTill;
	private Integer workgroupId;
	private Integer userId;

	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTill() {
		return dateTill;
	}
	public void setDateTill(Date dateTill) {
		this.dateTill = dateTill;
	}
	public Integer getWorkgroupId() {
		return workgroupId;
	}
	public void setWorkgroupId(Integer workgroupId) {
		this.workgroupId = workgroupId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}