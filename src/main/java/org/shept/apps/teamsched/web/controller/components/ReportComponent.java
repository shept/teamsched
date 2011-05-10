package org.shept.apps.teamsched.web.controller.components;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.hibernate.Session;
import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.view.TimesheetReportRow;
import org.shept.apps.teamsched.web.controller.commands.ReportsCommand;
import org.shept.apps.teamsched.web.security.AuthenticationUtils;
import org.shept.org.springframework.web.bind.support.ComponentDataBinder;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentToken;
import org.shept.org.springframework.web.servlet.mvc.delegation.component.AbstractComponent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;


/** 
 * @version $$Id: ReportComponent.java,v 1.1 2009/11/27 18:53:22 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class ReportComponent extends AbstractComponent {
	
	private static JasperReport report1 = null;    

	private HibernateDaoSupport dao;
	
	public ModelAndView excecuteAction(HttpServletRequest request,
			HttpServletResponse response, ComponentToken token)
			throws Exception {
		String method = token.getToken().getMethod();

		if (method.equals("onReport")) {
			if (logger.isDebugEnabled()) {
				logger.debug("Perform Reporting with token: " + token.toString());
			}
			return onReport(request, response, token);
		}
		return modelUnhandled(request, token);	
	}


	@SuppressWarnings("unchecked")
	protected ModelAndView onReport(HttpServletRequest request, HttpServletResponse response, ComponentToken token) throws Exception {
		
		ComponentDataBinder binder = (ComponentDataBinder) token.getBinder();
		binder.bindAndValidate(new ServletWebRequest(request), token);
		if ( hasErrorsInPath(binder.getBindingResult(), token, "" )) {
			return modelWithErrors(binder);
		}
		
		// Session session = getDao().getSessionFactory().openSession();	
		// erzeugt eine neue Session, die müssten wir auch wieder wegwerfen, Fehlerbehandlung, e.t.c.
		// Wir nehmen immer aber lieber die vorhandene Session  (die ist an den Thread gebunden und wird automatisch weggeworfen);
		@SuppressWarnings("unused")
		Session session = SessionFactoryUtils.getSession(getDao().getSessionFactory(), false);
		ReportsCommand fbo = (ReportsCommand) token.getComponent();

		String strName = getMessageSourceAccessor().getMessage("nameColon");
		String strDate = getMessageSourceAccessor().getMessage("dateColon");
		String strFrom = getMessageSourceAccessor().getMessage("from");
		String strUntill = getMessageSourceAccessor().getMessage("untill");
		String strTime = getMessageSourceAccessor().getMessage("time");
		String strWorkgroup = getMessageSourceAccessor().getMessage("workgroup");
		String strIssue = getMessageSourceAccessor().getMessage("issue");
		String strComment = getMessageSourceAccessor().getMessage("comment");
		String strTotalSum = getMessageSourceAccessor().getMessage("label.totalSum");
		String strPeriodsRecorded = getMessageSourceAccessor().getMessage("label.periodsRecorded");
		String strPage = getMessageSourceAccessor().getMessage("page");

		String dateFormatHeader = getMessageSourceAccessor().getMessage("dateFormatHeader");
		String dateFormat = getMessageSourceAccessor().getMessage("dateFormat");
		
		Calendar dateFrom = Calendar.getInstance();
		dateFrom.setTime(fbo.getDateFrom());
		Calendar dateTill = Calendar.getInstance();
		dateTill.setTime(fbo.getDateTill());
	    dateTill.add(Calendar.DAY_OF_YEAR, 1);
	    
	    Integer workgroupId = fbo.getWorkgroupId();
	    Integer userId = fbo.getUserId();
	    String userName ="";
		if (userId == null){
			User usr = (User) AuthenticationUtils.getUser();
			userId = usr.getId();
			userName = usr.getUsername();
		}
		else {
			User user = dao.getHibernateTemplate().load(User.class, userId);
			userName = user.getUsername();
		}
		
	    JasperPrint jasperPrint;
    
	    Map parameter = new HashMap();

	    parameter.put("user", userName);
	    parameter.put("dateFormatHeader", dateFormatHeader);
	    parameter.put("dateFormat", dateFormat);
	    parameter.put("dateFrom", dateFrom);
	    parameter.put("dateTill", dateTill);
	    parameter.put("strName", strName);
	    parameter.put("strDate", strDate);
	    parameter.put("strFrom", strFrom);
	    parameter.put("strUntill", strUntill);
	    parameter.put("strTime", strTime);
	    parameter.put("strWorkgroup", strWorkgroup);
	    parameter.put("strIssue", strIssue);
	    parameter.put("strComment", strComment);
	    parameter.put("strTotalSum", strTotalSum);
	    parameter.put("strPeriodsRecorded", strPeriodsRecorded);
	    parameter.put("strPage", strPage);
	    
	    // parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
	
	     List<TimesheetReportRow> results;
	     if (workgroupId == null)
	    	 results = getDao().getHibernateTemplate().
 	 				findByNamedQueryAndNamedParam("qryTimesheetReport", 
 	 						new String[] {"userId", "dateFrom", "dateTill" }, 
 	 						new Object[] {userId, dateFrom, dateTill});
	     else
    	 	results = getDao().getHibernateTemplate().
	 				findByNamedQueryAndNamedParam("qryTimesheetReportOneWorkgroup", 
	 						new String[] {"userId", "dateFrom", "dateTill", "workgroupId" }, 
	 						new Object[] {userId, dateFrom, dateTill, workgroupId});
	     
	     //if (results.size() == 0 ) {
	    	// binder.getBindingResult().reject("reports.reportEmpty");
	    	 //return modelWithErrors(binder);
	    //}
	    
		try
	    {
	        if (report1 == null) {
	        	ClassPathResource cpr = new ClassPathResource("reports/ReportTest.jrxml");
				report1 = JasperCompileManager.compileReport(cpr.getInputStream());
	        }
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(results);
			jasperPrint = JasperFillManager.fillReport(report1, parameter, ds);
			response.setContentType("application/pdf");
			OutputStream out = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
			return null;
			
	    }
	    catch (JRException e)
	    {
	    	logger.error("There was an error creating the report for user " + userId.toString(), e);
	    	binder.getBindingResult().reject("reports.unexpected");
	    }
	    return modelWithErrors(binder);
	}


	@Override
	public Map<String, String> getDefaultMappings() {
		Map<String, String> mappings = new HashMap<String, String>();
		mappings.put("submitReport", "onReport");	// perform report
		return mappings;
	}


	public boolean supports(Object commandObject) {
		return commandObject instanceof ReportsCommand;
	}


	/**
	 * @return the dao
	 */
	public HibernateDaoSupport getDao() {
		return dao;
	}


	/**
	 * @param dao the dao to set
	 */
	@Resource
	public void setDao(HibernateDaoSupport dao) {
		this.dao = dao;
	}

}
