/**
 * 
 */
package org.shept.apps.teamsched.orm.view;

import java.util.Calendar;
import java.util.Date;



/** 
 * @version $$Id: TimesheetReportRow.java,v 1.1 2009/11/27 18:53:22 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class TimesheetReportRow {
	
	private long id;
	
	private int userId;
	
	private Calendar dateFrom;
	
	private Calendar dateTill;
	
	private String workgroup;
	
	private String issue1;
	
	private String issue2;
	
	private String issue3;
	
	private String comment;
	
	/**
	 * @param id
	 * @param userId
	 * @param dateFrom
	 * @param dateTill
	 * @param workgroup
	 * @param issue1
	 * @param issue2
	 * @param issue3
	 * @param comment
	 */
	public TimesheetReportRow(Long id, Integer userId, Calendar dateFrom,
			Calendar dateTill, String workgroup, String issue1, String issue2,
			String issue3, String comment) {
		super();
		this.id = id;
		this.userId = userId;
		this.dateFrom = dateFrom;
		this.dateTill = dateTill;
		this.workgroup = workgroup;
		this.issue1 = issue1;
		this.issue2 = issue2;
		this.issue3 = issue3;
		this.comment = comment;
	}


	public TimesheetReportRow() {
		super();
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @return the dateFrom
	 */
	public Date getDatetimefrom() {
		return dateFrom.getTime();
	}

	/**
	 * @return the dateTill
	 */
	public Date getDatetimetill() {
		return dateTill.getTime();
	}

	/**
	 * @return the workgroup
	 */
	public String getWorkgroup() {
		return workgroup;
	}

	/**
	 * @return the issue1
	 */
	public String getIssue1() {
		return (null == issue1 ? "" : issue1);
	}

	/**
	 * @return the issue2
	 */
	public String getIssue2() {
		return (null == issue2 ? "" : issue2);
	}

	/**
	 * @return the issue3
	 */
	public String getIssue3() {
		return (null == issue3 ? "" : issue3);
	}
	
	public String getIssueString() {
		String ret = getIssue3();
		if (ret.length() > 0 ) ret = ret + " -> ";
		ret = ret + getIssue2();
		if (ret.length() > 0 ) ret = ret + " -> ";
		ret = ret + getIssue1();
		return ret;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	

}
