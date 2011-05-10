/**
 * 
 */
package org.shept.apps.teamsched.web.controller.components;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.Userinvitation;
import org.shept.apps.teamsched.orm.UserinvitationId;
import org.shept.apps.teamsched.orm.Userworkgroup;
import org.shept.apps.teamsched.web.controller.filters.SearchUserFilter;
import org.shept.apps.teamsched.web.security.AuthenticationUtils;
import org.shept.org.springframework.beans.support.CommandWrapper;
import org.shept.org.springframework.beans.support.PageableList;
import org.shept.org.springframework.beans.support.Refreshable;
import org.shept.org.springframework.orm.hibernate3.support.HibernateDaoSupportExtended;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentToken;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentUtils;
import org.shept.org.springframework.web.servlet.mvc.delegation.SubCommandProvider;
import org.shept.org.springframework.web.servlet.mvc.delegation.component.AbstractPersistenceComponent;
import org.shept.util.PageHolderFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class UserInvitationComponent extends AbstractPersistenceComponent implements InitializingBean {
	
	private PageHolderFactory pageHolderFactory;

	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.servlet.mvc.delegation.component.AbstractPersistenceComponent#doActionInternal(javax.servlet.http.HttpServletRequest, org.shept.org.springframework.web.servlet.mvc.delegation.ComponentToken, org.springframework.web.bind.ServletRequestDataBinder)
	 *
	 * handling userInvitations.tagx
	 */
	@Override
	protected ModelAndView doActionInternal(HttpServletRequest request, ComponentToken token)
			throws Exception {
		String method = token.getToken().getMethod();
		 
		if (method.equals("onInvitationAccept")) {
			if (logger.isDebugEnabled()) {
				logger.debug("Invitation accepted: " + token.toString());
			}
			return doInvitation(request, token, true);
			
		} else if (method.equals("onInvitationRefuse")) {
			if (logger.isDebugEnabled()) {
				logger.debug("Invitation refused: " + token.toString());
			}
			return doInvitation(request, token, false);

		} else if (method.equals("onUserInvite")) {
			if (logger.isDebugEnabled()) {
				logger.debug("Invite user (add to invitation list): " + token.toString());
			}
			return doInviteUser(request, token);

		} else if (method.equals("onUserNew")) {
			if (logger.isDebugEnabled()) {
				logger.debug("New user (open search): " + token.toString());
			}
			return doNewUser(request, token);

		} else if (method.equals("onUserActivate")) {
			if (logger.isDebugEnabled()) {
				logger.debug("User activation: " + token.toString());
			}
			return doUserActivation(request, token);
			
		} else if (method.equals("onUserDelete")) {
			if (logger.isDebugEnabled()) {
				logger.debug("User deletion: " + token.toString());
			}
			return doUserDeletion(request, token);
			
		}		
		return modelUnhandled(request, token);
	}

	/**
	 * remove user from invitation list
	 * 
	 * @param
	 * @return
	 *
	 * @param request
	 * @param token
	 * @param binder
	 * @return
	 * @throws Exception 
	 */
	private ModelAndView doUserDeletion(HttpServletRequest request, ComponentToken token) throws Exception {
		Object model = ComponentUtils.getModelFromList(token);
		User hostUser = (User) AuthenticationUtils.getUser();
		User guestUser = (User) (((Object[]) model)[0]);
		deleteUserWorkgroups(hostUser, guestUser, (HibernateDaoSupportExtended) getDao());

		if (token.getComponent() instanceof Refreshable) {
			((Refreshable) token.getComponent()).refresh();
		}
		return modelRedirectClip(request, token);
	}

	private ModelAndView doUserActivation(HttpServletRequest request, ComponentToken token) throws Exception {
		Object model = ComponentUtils.getModelFromList(token);
		User selUser = (User) (((Object[]) model)[0]);
		selUser.setBactive(!selUser.getBactive());
		txSaveModel(selUser);
		return modelRedirectClip(request, token);
	}

	private ModelAndView doInviteUser(HttpServletRequest request,
			ComponentToken token) throws Exception {
		Object model = ComponentUtils.getModelFromList(token);
		User hostUser = (User) AuthenticationUtils.getUser();
		User selUser = (User) model;

		// Host and Invited users must be different
		if (hostUser.equals(selUser)) {
			return modelRedirectClip(request, token);
		}

		UserinvitationId invId = new UserinvitationId(hostUser.getId(), selUser.getId());
		Userinvitation inv = new Userinvitation(invId);
		inv.setDatetimeInvitation(Calendar.getInstance());
		txSaveModel(inv);
		
		CommandWrapper cw = ComponentUtils.lookupComponentWrapper(token, -1);
		if (cw != null && cw.getCommand() instanceof Refreshable) {
			((Refreshable)cw.getCommand()).refresh();
		}
		return modelRedirectClip(request, token);
	}

	private ModelAndView doInvitation(final HttpServletRequest request,
			ComponentToken token, boolean bAccept) throws Exception {
		Object model = ComponentUtils.getModelFromList(token);
		final Userinvitation uinv = (Userinvitation) (((Object[]) model)[1]);
		uinv.setDatetimeResponse(Calendar.getInstance());
		uinv.setBaccepted(bAccept);
		txSaveModel(uinv);
		return modelRedirectClip(request, token);
	}

	private ModelAndView doNewUser(HttpServletRequest request,
			ComponentToken token) throws Exception {
		ComponentUtils.removeComponentsAfterIndex(token, null);
		CommandWrapper cw = new CommandWrapper();
		Refreshable pageHolder = pageHolderFactory.getObject();

		String searchHint = getMessageSourceAccessor().getMessage("phrase.searchHint"); 
		SearchUserFilter filter = new SearchUserFilter();
		filter.setSearch(searchHint);
		filter.setSearchHint(searchHint);
		pageHolder.setFilter(filter);
		pageHolder.refresh();
		cw.setCommand(pageHolder);
		cw.setTagName("userInvite");
		SubCommandProvider scp = (SubCommandProvider) token.getCommand();	
		scp.getChildren().add(cw);
		return modelRedirect(request, token,scp.getChildren().size());
	}

	protected void deleteUserWorkgroups( final User hostUser, final User guestUser, final HibernateDaoSupportExtended dao) throws Exception {
		doInTransactionIfAvailable(new TransactionCallback<Object>() {
			public Object doInTransaction(TransactionStatus status) {
				txDeleteUserWorkgroups( hostUser,guestUser, dao);
				return null;
			}
		});		
	}

	/**
	 * 
	 * @param
	 * @return
	 *
	 * @param hostUser
	 * @param guestUser
	 * @param dao
	 */
	@SuppressWarnings("unchecked")
	protected void txDeleteUserWorkgroups( User hostUser, User guestUser, HibernateDaoSupportExtended dao) {
		// delete all userWorkgroup entries for the selected user
		List<Userworkgroup> uwg = (List<Userworkgroup>) dao.getHibernateTemplateExtended().
			findByNamedQueryAndNamedParam("qryUserWorkgroupsByHostAndUser", 
					new String[ ] {"userHostId", "userInvitedId"}, new Integer[]{hostUser.getId(), guestUser.getId() });
		for (Userworkgroup userworkgroup : uwg) {
			dao.getHibernateTemplateExtended().delete(userworkgroup);
		}

		UserinvitationId invId = new UserinvitationId(hostUser.getId(), guestUser.getId());
		Userinvitation inv = new Userinvitation(invId);
		dao.getHibernateTemplateExtended().delete(inv);
	}
	
	/**
	 * 
	 */
	protected void txSaveModel(final Object model) throws Exception {
		doInTransactionIfAvailable(new TransactionCallback<Object>() {
			public Object doInTransaction(TransactionStatus status) {
				doSaveModel(model);
				return null;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.servlet.mvc.component.ComponentHandler#supports(java.lang.Class)
	 */
	public boolean supports(Object commandObject) {
		return commandObject instanceof PageableList;
	}

	@Override
	public Map<String, String> getDefaultMappings() {
		Map<String, String> mappings = new HashMap<String, String>();
		mappings.put("submitInvitationAccept", "onInvitationAccept");	// accept invitation
		mappings.put("submitInvitationRefuse", "onInvitationRefuse");	// refuse invitation
		mappings.put("submitUserInvitation", "onUserInvite");				// activate user
		mappings.put("submitUserActivate", "onUserActivate");				// activate user
		mappings.put("submitUserDelete", "onUserDelete");						// delete user
		mappings.put("submitUserNew", "onUserNew");						// new user
		return mappings;
	}

	/**
	 * @return the pageHolderFactory
	 */
	public PageHolderFactory getPageHolderFactory() {
		return pageHolderFactory;
	}

	/**
	 * @param pageHolderFactory the pageHolderFactory to set
	 */
	@Resource
	public void setPageHolderFactory(PageHolderFactory pageHolderFactory) {
		this.pageHolderFactory = pageHolderFactory;
	}

	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.servlet.mvc.delegation.component.AbstractPersistenceComponent#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		Assert.notNull(getPageHolderFactory(), "PageHolderFactory must not be null");			
	}

	
}
