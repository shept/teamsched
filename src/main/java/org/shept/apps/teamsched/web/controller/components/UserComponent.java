package org.shept.apps.teamsched.web.controller.components;

import java.util.Calendar;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.web.controller.commands.UserAdminEditMode;
import org.shept.apps.teamsched.web.controller.commands.UserCommand;
import org.shept.apps.teamsched.web.controller.commands.UserConfirmTermsCommand;
import org.shept.apps.teamsched.web.controller.commands.WorkgroupSelectMode;
import org.shept.apps.teamsched.web.controller.filters.WorkgroupModeFilter;
import org.shept.apps.teamsched.web.security.AuthenticationUtils;
import org.shept.apps.teamsched.web.support.IdentService;
import org.shept.org.springframework.beans.support.CommandWrapper;
import org.shept.org.springframework.beans.support.Refreshable;
import org.shept.org.springframework.web.bind.support.ComponentDataBinder;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentToken;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentUtils;
import org.shept.org.springframework.web.servlet.mvc.delegation.component.DefaultProperties;
import org.shept.org.springframework.web.servlet.mvc.delegation.component.EntityPersistenceComponent;
import org.shept.org.springframework.web.servlet.mvc.support.InfoItem;
import org.shept.util.PageHolderFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


/** 
 * @version $$Id: ReportComponent.java,v 1.1 2009/11/27 18:53:22 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class UserComponent extends EntityPersistenceComponent {
	
	private IdentService identService;
	
	private String successView;
	
	private PageHolderFactory pageHolderFactory;


//	private WorkgroupModeSelectionComponent wgComponent;

	@Override
	public ModelAndView excecuteAction(HttpServletRequest request,
			HttpServletResponse response, ComponentToken token)
			throws Exception {
		String method = token.getToken().getMethod();

		if (method.equals("onChangePassword")) {
			if (logger.isDebugEnabled()) {
				logger.debug("Password change with token: " + token.toString());
			}
			return onChangePasword(request, response, token);
		}
		if (method.equals("onForgottenUsername")) {
			if (logger.isDebugEnabled()) {
				logger.debug("Username forgotten with token: " + token.toString());
			}
			return onForgottenUsername(request, response, token);
		}
		if (method.equals("onConfirmTerms")) {
			if (logger.isDebugEnabled()) {
				logger.debug("Confim terms and conditions with token: " + token.toString());
			}
			return onConfirmTerms(request, response, token);
		}
		ModelAndView mav = super.excecuteAction(request, response, token);
		// reset the repeat captcha
		UserCommand fbo = (UserCommand) token.getComponent();
		fbo.setRepeatCaptcha("");
		return mav;
	}

	protected ModelAndView onConfirmTerms(HttpServletRequest request,
			HttpServletResponse response, ComponentToken token) throws Exception {
		ComponentDataBinder binder = (ComponentDataBinder) token.getBinder();
		binder.bind(new ServletWebRequest(request));

		Boolean confirmed = ((UserConfirmTermsCommand) token.getComponent()).getBterms();
		final User usr = (User) AuthenticationUtils.getUser();
		if (confirmed && usr != null) {
			usr.setDatTerms(Calendar.getInstance());
			upgradeUserRole(usr);
		}
		return modelRedirect(request, token);
	}


	/**
	 * @param
	 * @return
	 *
	 * @param usr
	 * @throws Exception
	 */
	private void upgradeUserRole(final User usr) throws Exception {
		doInTransactionIfAvailable(new TransactionCallback<Object>() {
			public Object doInTransaction(TransactionStatus status) {
				doSaveModel(usr);
				Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						usr, usr.getUserpassword(), usr.getAuthorities());
				auth.setDetails(details);
				SecurityContextHolder.getContext().setAuthentication(auth);
				return null;
			}
		});
	}


	protected ModelAndView onForgottenUsername(HttpServletRequest request,
			HttpServletResponse response, ComponentToken token) {
		ComponentDataBinder binder = (ComponentDataBinder) token.getBinder();
		binder.bind(new ServletWebRequest(request));

		UserCommand fbo = (UserCommand) token.getComponent();
		String mail = fbo.getForgotUsername();
		
		// let's do a very simple quick & dirty check beside the validation scheme
		if (mail.indexOf("@") <= 0) {
			String path = ComponentUtils.lookupComponentPath(token) + ".";
			binder.getBindingResult().rejectValue(path + "forgotUsername", "userForm.emailInvalid");
			return modelWithErrors(binder);
		}

		// save the user with the replacement from the validator
		
		identService.requestUsernameEmail(request, mail);
		ModelAndView mv = new ModelAndView();
		mv.setView(new RedirectView(successView, true, true, false));
		return mv;
	}


	protected ModelAndView onChangePasword(HttpServletRequest request, HttpServletResponse response, ComponentToken token) throws Exception {
		
		ComponentDataBinder binder = (ComponentDataBinder) token.getBinder();
		binder.bindAndValidate(new ServletWebRequest(request), token);
		if ( hasErrorsInPath(binder.getBindingResult(), token, "" )) {
			return modelWithErrors(binder);
		}
		UserCommand fbo = (UserCommand) token.getComponent();
		// save the user with the replacement from the validator
		
		identService.requestPasswordChange(request, fbo.getUser(), fbo.getRepeatPassword());
		ModelAndView mv = new ModelAndView();
		mv.setView(new RedirectView(successView, true, true, false));
		return mv;
	}
	
	
	@Override
	protected ModelAndView doSave(HttpServletRequest request,
			ComponentToken token) throws Exception {
		// get user notifications from user if email and password changed
		UserCommand cmd = (UserCommand) token.getComponent();
		cmd.verifiedOk();	// when we are here we have passed the validator
		User usr = cmd.getUser();
		
		if (cmd.isNewUser()) {
			usr.setBactive(false);
		}
		if (cmd.isNewUser() ||
				cmd.getEditMode().equals(UserAdminEditMode.USER_EDIT) ||
				cmd.getEditMode().equals(UserAdminEditMode.USER_ADMIN)) {
			if(StringUtils.hasText(cmd.getNewPassword())) {
				usr.setUserpassword(cmd.getNewPassword());
			}
 		}
		
		// set the owning user as a parent (if any)
		if (cmd.getEditMode().equals(UserAdminEditMode.USER_CREATE ) &&
				AuthenticationUtils.getUser() != null ) {
			usr.setParentUser((User)AuthenticationUtils.getUser());
			if (usr.getDatTerms() != null) {
				usr.setConfirmingUser((User) AuthenticationUtils.getUser());
			}
		}
		
		// finally save the user
		ModelAndView mav = super.doSave(request, token);

		if (cmd.isNewUser()) {
			if (! cmd.getUser().equals(cmd.getOldUser())) {
				// send user welcome message here
				identService.welcomeUser(request, usr, (User) AuthenticationUtils.getUser());
			}
		}
		else if (cmd.getEditMode().equals(UserAdminEditMode.USER_EDIT) ||
				cmd.getEditMode().equals(UserAdminEditMode.USER_ADMIN)) {
			if (StringUtils.hasText(cmd.getUser().getNewemail())) {
				// user to confirm eMail change
				identService.confirmEmailChange(request, usr);
			}
		}
		cmd.setUser(cmd.getUser());		// set oldUser = newUser in case we proceed editing this user
		
		// proceed to next form
		if (cmd.getEditMode().equals(UserAdminEditMode.USER_REGISTER)) {

			// set the filter
			WorkgroupModeFilter filter = new WorkgroupModeFilter();
			filter.setMode(WorkgroupSelectMode.GROUPS_OWNED);
			filter.setUser(cmd.getUser());
			// initialize the pageHolder
			Refreshable pageHolder = pageHolderFactory.getObject();
			pageHolder.setFilter(filter);
			pageHolder.refresh();
			// initialize the CommandWrapper and wrap the pageHolder
			CommandWrapper cw = new CommandWrapper();
			cw.setCommand(pageHolder);
			cw.setTagName("workgroupsFiltered");
			//initialize an info String ala 'create workgroups for User xyz'
			InfoItem infoItem = new InfoItem("info.newWorkgroups");
			infoItem.setSelector("username");
			String info = ComponentUtils.getComponentInfo(request, infoItem, cmd.getUser());
			cw.getProperties().put(DefaultProperties.INFO, info);									
			// apply configuration and attach component
			ComponentUtils.applyConfiguration(cw, getApplicationContext());
			ComponentUtils.addComponent(token, cw);
			return modelRedirect(request, token);
		}
		
		return mav;
	}


	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.servlet.mvc.delegation.component.EntityPersistenceComponent#doCancel(javax.servlet.http.HttpServletRequest, org.shept.org.springframework.web.servlet.mvc.delegation.ComponentToken)
	 */
	@Override
	protected ModelAndView doCancel(HttpServletRequest request,
			ComponentToken token) {
//		token.getBinder().bind(new ServletWebRequest(request));
		return modelRedirectClip(request, token);
	}

	/* (non-Javadoc)
	 * @see org.shept.org.springframework.web.servlet.mvc.delegation.component.AbstractComponent#getMappings()
	 */
	@Override
	public Map<String, String> getMappings() {
		Map<String, String> mappings = super.getMappings();
		mappings.put("submitSaveUser", "onSave");				// save the user
		mappings.put("submitCancelUser", "onCancel");
		mappings.put("submitChangePassword", "onChangePassword");
		mappings.put("submitForgottenUsername", "onForgottenUsername");
		mappings.put("submitConfirmTerms", "onConfirmTerms");
		return mappings;
	}
	


	@Override
	public boolean supports(Object commandObject) {
		return commandObject instanceof UserCommand ||
			commandObject instanceof UserConfirmTermsCommand;
	}


	/**
	 * @return the identService
	 */
	public IdentService getIdentService() {
		return identService;
	}


	/**
	 * @param identService the identService to set
	 */
	public void setIdentService(IdentService identService) {
		this.identService = identService;
	}


	/**
	 * @param succesView the succesView to set
	 */
	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	/**
	 * @param pageHolderFactory the pageHolderFactory to set
	 */
	@Resource
	public void setPageHolderFactory(PageHolderFactory pageHolderFactory) {
		this.pageHolderFactory = pageHolderFactory;
	}

}
