package org.shept.apps.teamsched.orm.view;

import java.io.Serializable;


/** 
 * @version $$Id: CountObject.java,v 1.1 2009/11/27 18:53:46 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class CountObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	
	private long count;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the count
	 */
	public long getCount() {
		return count;
	}

	/**
	 * @param id
	 * @param count
	 */
	public CountObject(int id, int count) {
		super();
		this.id = id;
		this.count = count;
	}
	
	/**
	 * @param id
	 * @param count
	 */
	public CountObject(long id, int count) {
		super();
		this.id = id;
		this.count = count;
	}

	/**
	 * @param id
	 * @param count
	 */
	public CountObject(int id, long count) {
		super();
		this.id = id;
		this.count = count;
	}

	/**
	 * @param id
	 * @param count
	 */
	public CountObject(long id, long count) {
		super();
		this.id = id;
		this.count = count;
	}

}
