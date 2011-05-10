/**
 * 
 */
package org.shept.apps.teamsched.web.controller.filters;

import java.io.Serializable;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.shept.apps.teamsched.orm.User;
import org.shept.persistence.ModelCreation;
import org.shept.persistence.provider.hibernate.HibernateCriteriaDefinition;
import org.springframework.beans.support.SortDefinition;
import org.springframework.util.StringUtils;

/** 
 * @version $$Id: $$
 *
 * @author Andi
 *
 */
public class SearchUserFilter implements HibernateCriteriaDefinition, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String search;
	
	private String searchHint;
	

	/* (non-Javadoc)
	 * @see org.shept.persistence.provider.hibernate.HibernateCriteriaDefinition#getCriteria(org.springframework.beans.support.SortDefinition)
	 */
	public DetachedCriteria getCriteria(SortDefinition sortDefinition) {
		DetachedCriteria crit = DetachedCriteria.forClass(User.class);
		DetachedCriteria empty = DetachedCriteria.forClass(User.class);
		empty.add(Restrictions.isNull("id"));
		String src = StringUtils.trimWhitespace(search);
		
		if (StringUtils.hasText(src)) {
			if (search.equalsIgnoreCase(searchHint)) {
				return empty;
			}
			if (src.indexOf("@") > 0) {		// email
				crit.add(Restrictions.eq("email", getSearch()));
			} else if (StringUtils.containsWhitespace(src)) {		// firstname & name
				String[] names = StringUtils.tokenizeToStringArray(src, " \t\n\r\f");
				if (names.length > 2) {
					return empty;
				}
				if (names.length == 2) {
					crit.add(Restrictions.eq("firstname", names[0]));
					crit.add(Restrictions.eq("name", names[1]));					
				}
			} else {	// username
				crit.add(Restrictions.eq("username", src));
			}
			return crit;
		} else {
			return empty;
		}
	}


	public ModelCreation getNewModelTemplate() {
		User usr = User.getInstance();
		String src = StringUtils.trimWhitespace(search);
		if (StringUtils.hasText(src)) {
			if (src.indexOf("@") > 0) {		// email
				usr.setEmail(src);
			} else if (StringUtils.containsWhitespace(src)) {		// firstname & name
				String[] names = StringUtils.tokenizeToStringArray(src, " \t\n\r\f");
				if (names.length > 2) {
					return null;
				}
				if (names.length == 2) {
					usr.setFirstname(names[0]);
					usr.setName(names[1]);					
				}
			} else {	// username
				usr.setUsername(src);
			}
		}
		return usr; 
	}

	/**
	 * @return the search
	 */
	public String getSearch() {
		return search;
	}


	/**
	 * @param search the search to set
	 */
	public void setSearch(String search) {
		this.search = search;
	}


	/**
	 * @return the searchHint
	 */
	public String getSearchHint() {
		return searchHint;
	}


	/**
	 * @param searchHint the searchHint to set
	 */
	public void setSearchHint(String searchHint) {
		this.searchHint = searchHint;
	}


}
