/**
 * 
 */
package org.shept.apps.teamsched.web.controller.commands;

/** 
 * @version $$Id: UserAdminEditMode.java,v 1.1 2009/11/27 18:53:10 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public enum UserAdminEditMode {
	USER_ADMIN, 			/* admin another user account */
	USER_EDIT, 				/* edit OWN user account */
	USER_CREATE, 			/* create another user account (for inviting another user) */
	USER_REGISTER,			/* register yourself as a new user */
	USER_FORGOT_PASSWORD;	/* (re-) set (change) your password */ 
} ;

