/**
 * 
 */
package org.shept.apps.teamsched.web.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.shept.org.springframework.web.servlet.mvc.delegation.SheptController;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.web.servlet.ModelAndView;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class MaintenanceController extends SheptController {
	
	private Integer historySizeInDays = 730; 	// 2 years is default
	
	private String schema;
	
	private DataSource dataSource;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ModelAndView mav = super.handleRequestInternal(request, response);
		
		Calendar delDate = Calendar.getInstance();
		delDate.add(Calendar.DAY_OF_YEAR, historySizeInDays * -1);
		Connection conn = null;
		CallableStatement statement = null;
		String proc = "pradmdeletehist";
		if (StringUtils.hasText(schema)) {
			proc = schema + "." + proc;
		}

		try {
			conn = dataSource.getConnection();
			statement = conn.prepareCall(" {? = Call " + proc + " ( ? ) } ");
			statement.registerOutParameter(1, java.sql.Types.INTEGER);
			statement.setTimestamp(2, new Timestamp(delDate.getTime().getTime()));
			@SuppressWarnings("unused")
			Object retval = statement.execute();
			logger.info("Cleanup database successful");
		} catch (SQLException e) {
			logger.error("Cleanup database threw exception", e);
			
			BindingResult res = BindingResultUtils.getBindingResult(
					mav.getModelMap(), getCommandName(""));
			res.reject("maintenance.error", 
					new String[]{e.getMessage()}, "");
			return mav;
			
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (Exception ex) {
			}
		}

		return mav;
		
	}

	/**
	 * @param historySizeInDays the historySizeInDays to set
	 */
	public void setHistorySizeInDays(Integer historySizeInDays) {
		this.historySizeInDays = historySizeInDays;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @param schema the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}


}
