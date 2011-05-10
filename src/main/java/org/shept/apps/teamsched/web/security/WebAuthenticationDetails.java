/**
 * 
 */
package org.shept.apps.teamsched.web.security;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;


/** 
 * @version $$Id: WebAuthenticationDetails.java,v 1.1 2009/11/27 18:53:24 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class WebAuthenticationDetails extends org.springframework.security.web.authentication.WebAuthenticationDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6200831974649338978L;


	public WebAuthenticationDetails(HttpServletRequest request) {
		super(request);
	}

	private String userAgent;
	
	private String screenResolution;
	
	private String hostAddressForwarded;
	
	private String referer;
	
	private String host;

	
	/* (non-Javadoc)
	 * @see org.springframework.security.ui.WebAuthenticationDetails#doPopulateAdditionalInformation(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void doPopulateAdditionalInformation(HttpServletRequest request) {
        Enumeration names = request.getHeaderNames();
        setUserAgent(request.getHeader("user-agent"));
        setScreenResolution("unknown");
        setHostAddressForwarded(request.getHeader("X-Forwarded-For"));
        setHost(request.getHeader("host"));
        setReferer(request.getHeader("referer"));
	}

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * @return the screenResolution
	 */
	public String getScreenResolution() {
		return screenResolution;
	}

	/**
	 * @param screenResolution the screenResolution to set
	 */
	public void setScreenResolution(String screenResolution) {
		this.screenResolution = screenResolution;
	}

	/**
	 * @return the hostAddressForwarded
	 */
	public String getHostAddressForwarded() {
		return hostAddressForwarded;
	}

	/**
	 * @param hostAddressForwarded the hostAddressForwarded to set
	 */
	public void setHostAddressForwarded(String hostAddressForwarded) {
		this.hostAddressForwarded = hostAddressForwarded;
	}

	/**
	 * @return the referer
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * @param referer the referer to set
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

}
