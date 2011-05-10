package org.shept.apps.teamsched.web.controller;

import java.awt.image.BufferedImage;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shept.apps.teamsched.web.support.SessionData;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

/** 
 * @version $Rev: 34 $
 * @date $Date: 2010-05-03 16:32:07 +0200 (Mo, 03 Mai 2010) $
 * @author $Author: aha $
 *
 */
public class CaptchaController extends AbstractController {
	
	private ImageCaptchaService captchaService;
	
	protected SessionData sessionData;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BufferedImage challenge = null;
		try {
			// get the session id that will identify the generated captcha.
			// the same id must be used to validate the response, the session id
			// is a good candidate!
			String captchaId = request.getSession().getId();
			sessionData.setSessionId(captchaId);

			// call the ImageCaptchaService getChallenge method
			challenge = captchaService.getImageChallengeForID(
					captchaId, request.getLocale());

		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} catch (CaptchaServiceException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}

		// flush it in the response
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response
				.getOutputStream();
		ImageIO.write(challenge, "jpeg", responseOutputStream);
		responseOutputStream.flush();
		responseOutputStream.close();
		return null;
	}

	/**
	 * @param captchaController the captchaController to set
	 */
	public void setCaptchaService(ImageCaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	/**
	 * @param sessionData the sessionData to set
	 */
	@Resource
	public void setSessionData(SessionData sessionData) {
		this.sessionData = sessionData;
	}

}
