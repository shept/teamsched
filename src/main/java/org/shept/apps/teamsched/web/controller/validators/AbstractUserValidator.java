package org.shept.apps.teamsched.web.controller.validators;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shept.apps.teamsched.web.controller.commands.UserValidatableCommand;
import org.shept.apps.teamsched.web.support.SessionData;
import org.shept.org.springframework.orm.hibernate3.support.HibernateDaoSupportExtended;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.octo.captcha.service.image.ImageCaptchaService;

/** 
 * @version $$Id: UserValidator.java,v 1.1 2009/11/27 18:53:10 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public abstract class AbstractUserValidator implements Validator, ComponentValidator {

	/** Logger that is available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	protected HibernateDaoSupportExtended dao;

	protected SessionData sessionData;
	
	protected ImageCaptchaService captchaService;
		

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return UserValidatableCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		validate(target, errors, "");
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public abstract void validate(Object target, Errors errors, String path) ;
	
	/*
	 * It would be nice to have a better validation scheme. However its not simple to do this.
	 * We could adapt the official RFC 2822 regex validation. this is full of escape sequences however and
	 * needs to be tested when converting it to a java String.
	 * Simpler implementations however lack support for valid emails containing international characters
	 * i.e. hänschen@hänselundgretel.de
	 * 
	 * On the other hand we always verify by sending mails and look for a response, so we just make it very simple here. 
	 */
	protected boolean validateEmail(String mail) {
		if (mail.indexOf("@") > 0) return true;
		return false;
	}
		
	
	/**
	 * @param
	 * @return
	 *
	 * @param fbo
	 * @param errors
	 * @param path
	 * @return
	 */
	protected boolean validateCaptcha(UserValidatableCommand fbo, Errors errors, String path) {
		
		// checking the captcha
	
		boolean captchaOk = false;
		try {
			captchaOk = captchaService.validateResponseForID(sessionData
					.getSessionId(), fbo.getRepeatCaptcha());
		} catch (Exception ex) {
		}
		if (!captchaOk) {
			errors.rejectValue("repeatCaptcha",
					"userForm.captchaConfirmation");
			return false;
		}
		return true;
	}

	/**
	 * @param captchaService the captchaService to set
	 */
	@Resource
	public void setCaptchaService(ImageCaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	/**
	 * @param dao the dao to set
	 */
	@Resource
	public void setDao(HibernateDaoSupportExtended dao) {
		this.dao = dao;
	}

	/**
	 * @param sessionData the sessionData to set
	 */
	@Resource
	public void setSessionData(SessionData sessionData) {
		this.sessionData = sessionData;
	}


}
