package org.shept.apps.teamsched.web.security;

import java.util.List;

import org.shept.apps.teamsched.orm.User;
import org.shept.apps.teamsched.orm.dao.TimeDao;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/** 
 * @version $$Id: UserAuthenticationService.java,v 1.1 2009/11/27 18:53:24 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class UserAuthenticationService implements UserDetailsService {
	
	private TimeDao dao;

	@SuppressWarnings("unchecked")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		List users = dao.getHibernateTemplate().
			findByNamedQueryAndNamedParam("qryUserByUsername", "name", username);
        if (users.size() == 0) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDetails user = (UserDetails) users.get(0);
        if (user instanceof User) {
        	User domainUser = (User) user;
        	Boolean tac = domainUser.getDatTerms() != null;
        	Boolean active = domainUser.getBactive();
        	if (active == null || ! active) {
        		throw new UserUnauthorizedException("Unconfirmed", user);
        	}
        }
        return user;
	}

	public TimeDao getDao() {
		return dao;
	}

	public void setDao(TimeDao dao) {
		this.dao = dao;
	}

}
