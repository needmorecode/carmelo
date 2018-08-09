package carmelo.examples.server.login.action;

import java.util.List;

import carmelo.hibernate.BaseDao;
import carmelo.json.ResponseType;
import carmelo.json.JsonUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import carmelo.examples.server.login.dao.UserDao;
import carmelo.examples.server.login.domain.User;
import carmelo.examples.server.login.dto.TestDto;
import carmelo.examples.server.login.service.UserService;
import carmelo.servlet.Request;
import carmelo.servlet.annotation.PassParameter;
import carmelo.servlet.annotation.SessionParameter;
import carmelo.session.Session;
import carmelo.session.SessionConstants;
import carmelo.session.SessionManager;
import carmelo.session.Users;

/**
 * an example action which is used for a user to register, login, loginout, ...
 * 
 * @author needmorecode
 *
 */
@Component
public class UserAction {
	
	@Autowired
	private UserService userService;
	
	/**
	 * register
	 * @param name
	 * @param password
	 * @return
	 */
	public byte[] register(@PassParameter(name = "name")String name, @PassParameter(name = "password")String password){
		return userService.register(name, password);
	}
	
	/**
	 * login
	 * @param name
	 * @param password
	 * @param request
	 * @return
	 */
	public byte[] login(@PassParameter(name = "name")String name, @PassParameter(name = "password")String password, Request request){
		return userService.login(name, password, request);
	}
	
	/**
	 * logout
	 * @param userId
	 * @return
	 */
	public byte[] logout(@SessionParameter(name = SessionConstants.USER_ID)int userId){
		return userService.logout(userId);
	}
	
	/**
	 * reconnect
	 * @param sessionId
	 * @param request
	 * @return
	 */
	public byte[] reconnect(@PassParameter(name = "sessionId")String sessionId, Request request){
		return userService.reconnect(sessionId, request);
	}
	
	/**
	 * do something
	 * @param id
	 * @return
	 */
	public byte[] doSomething(@SessionParameter(name = SessionConstants.USER_ID)int userId, @PassParameter(name = "id") int id){
		return userService.doSomething(userId, id);
	}
	
	/**
	 * do something else
	 * @param id
	 * @return
	 */
	public byte[] doSomething2(@PassParameter(name = "id") int id){
		return userService.doSomething2(id);
	}
}
