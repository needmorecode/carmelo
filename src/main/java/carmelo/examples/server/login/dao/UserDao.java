package carmelo.examples.server.login.dao;

import org.springframework.stereotype.Component;

import carmelo.examples.server.login.domain.User;
import carmelo.hibernate.BaseDao;

@Component
public class UserDao extends BaseDao<User, Integer>{

}
