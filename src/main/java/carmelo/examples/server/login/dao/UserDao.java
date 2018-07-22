package carmelo.examples.server.login.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import carmelo.examples.server.login.domain.User;
import carmelo.hibernate.BaseDao;

@Component
public class UserDao extends BaseDao<User, Integer>{
	
	/**
	 * get user by name
	 * @param name
	 * @param password
	 * @return
	 */
	@Transactional
	public User getUser(String name) {
		SimpleExpression exp1 = Restrictions.eq("name", name);
		Criteria criteria = this.createCriteria(exp1);
		List<User> users = (List<User>)criteria.list();
		if (users == null || users.isEmpty())
			return null;
		else
			return users.get(0);
	}

}
