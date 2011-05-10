/**
 * 
 */
package org.shept.apps.teamsched.web.support;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

/**
 * @version $$Id: MailService.java,v 1.1 2009/11/27 18:53:12 oops.oops Exp $$
 * 
 * @author Andi
 * 
 */
public class MailService extends ApplicationObjectSupport {

	private JavaMailSender mailSender;
	private VelocityConfigurer velocityConfig;
	private String returnAddress;

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
		}

	public void sendConfirmationEmail(final EmailFeedback email)  {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setSubject(email.getSubject());
				message.setTo(email.getRecipient());
				message.setFrom(returnAddress); 	// parameterized...
				Map<String,Object> model = email.getReferenceData();
				String lang = "/" + email.getLocale().getLanguage() + "/";
				String template = StringUtils.applyRelativePath(lang, email.getAction().getTemplate());
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityConfig.getVelocityEngine(), template, model);
				message.setText(text, true);
			}
		};
		try {
			this.mailSender.send(preparator);			
		} catch (Exception e) {
			logger.error("Sendmail failed", e);
		}
	}
	
	public VelocityConfigurer getVelocityConfig() {
		return velocityConfig;
	}

	public void setVelocityConfig(VelocityConfigurer velocityConfig) {
		this.velocityConfig = velocityConfig;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}
}