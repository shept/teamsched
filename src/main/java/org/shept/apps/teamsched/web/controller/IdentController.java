/**
 * 
 */
package org.shept.apps.teamsched.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shept.apps.teamsched.web.support.IdentService;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

/** 
 * @version $$Id: IdentController.java,v 1.1 2009/11/27 18:53:10 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class IdentController extends AbstractController {
	
	private IdentService identService;
	
	private String successView;
	
	public static String getQueryString(String token, Boolean accept) {
		return "token=" + token + "&accept=" + accept;
	}

	protected class IdentCommand {
		
		private String token;
		private Boolean accept;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public Boolean getAccept() {
			return accept;
		}

		public void setAccept(Boolean accept) {
			this.accept = accept;
		}
		
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IdentCommand fbo = new IdentCommand();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(fbo);
		binder.bind(request);
		identService.executeConfirmation(fbo.getToken(), fbo.getAccept());

		ModelAndView mv = new ModelAndView();
		mv.setView(new RedirectView(successView, true, true, false));
		return mv;
		
	}

	public void setIdentService(IdentService identService) {
		this.identService = identService;
	}

	/**
	 * @param successView the successView to set
	 */
	public void setSuccessView(String successView) {
		this.successView = successView;
	}


}
