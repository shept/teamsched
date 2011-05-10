package org.shept.apps.teamsched.orm.view;

import java.io.Serializable;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.Workgroup;

/** 
 * $$Id: SelectWorkgroupUserRow.java,v 1.1 2009/11/27 18:53:46 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class SelectWorkgroupUserRow implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	
	private Workgroup workgroup;
	
	private Boolean selection;
	
	private Boolean selected;

	/**
	 * @param user
	 * @param workgroup
	 * @param selection
	 * @param selected
	 */
	public SelectWorkgroupUserRow( Workgroup workgroup, User user,
			boolean selection) {
		super();
		this.user = user;
		this.workgroup = workgroup;
		this.selection = selection;
		this.selected = selection;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the workgroup
	 */
	public Workgroup getWorkgroup() {
		return workgroup;
	}

	/**
	 * @return the seletion
	 */
	public Boolean getSelection() {
		return selection;
	}

	/**
	 * @param selection the selection to set
	 */
	public void setSelection(Boolean selection) {
		this.selection = selection;
	}

	/**
	 * @return the selected
	 */
	public Boolean getSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}


}
