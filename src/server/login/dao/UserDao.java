package server.login.dao;

import org.springframework.stereotype.Component;

import server.login.domain.User;
import hibernate.BaseDao;

@Component
public class UserDao extends BaseDao<User, Integer>{

}
