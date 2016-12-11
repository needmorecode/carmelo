package server.login.action;

import java.util.List;

import hibernate.BaseDao;
import json.EnumResponseType;
import json.JsonUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import server.login.dao.UserDao;
import server.login.domain.User;
import server.login.dto.TestDto;
import servlet.Request;
import servlet.annotation.PassParameter;
import servlet.annotation.SessionParameter;
import session.Session;
import session.SessionConstants;
import session.SessionManager;
import session.Users;

@Component
public class UserAction {
	
	@Autowired
	private UserDao userDao;
	
	public byte[] create(){
		return null;
	}
	
	public byte[] login(@PassParameter(name = "name")String name, @PassParameter(name = "password")String password, Request request){
		//Session session = null;
		int userId = 1;
		Session session = SessionManager.getInstance().createSession();
		String sessionId = session.getSessionId();
		Users.addUser(userId, sessionId);
		request.getCtx().attr(SessionConstants.SESSION_ID).set(sessionId);
		System.out.println("sessionId" + sessionId);
		return JsonUtil.buildJson(EnumResponseType.SUCCESS, new TestDto(1, "name"));
		// return sesssion id
	}
	
	public byte[] logout(@SessionParameter(name = "userId")int userId){
		String sessionId = Users.getSessionId(userId);
		SessionManager.getInstance().destroySession(sessionId);
		Users.removeUser(userId);
		
		return JsonUtil.buildJson(EnumResponseType.SUCCESS, new TestDto(1, "name"));
	}
	
	public byte[] reconnect(@PassParameter(name = "sessionId")String sessionId, Request request){
		Session session = SessionManager.getInstance().getSession(sessionId);
		if (session == null){
			System.out.println("reconnect fail");
			return JsonUtil.buildJson(EnumResponseType.FAIL, "");
		}
		request.getCtx().attr(SessionConstants.SESSION_ID).set(sessionId);
		System.out.println("reconnect success");
		return JsonUtil.buildJson(EnumResponseType.SUCCESS, new TestDto(1, "name"));
	}
	
	@Transactional
	public byte[] doSomething(@PassParameter(name = "id") int id){
/*		System.out.println("find the action, haha!");
		List<Object[]> list = userDao.queryBySql("select * from user where id = 1");
		for (Object[] array : list){
			System.out.println(array[1]);
		}
		list = userDao.queryBySql("select * from user where id = 1");
		for (Object[] array : list){
			System.out.println(array[1]);
		}*/
		User user =userDao.get(1);
		userDao.getSession().evict(user);
		user = userDao.get(1);
		/*Criteria c = userDao.createCriteria();
		c.add(Restrictions.lt("id", 100));//属性name的值是name
		c.add(Restrictions.gt("id", 1));
		//c.setCacheable(true);
		List<User> list=c.list();
		
		c = userDao.createCriteria();
		c.add(Restrictions.lt("id", 100));//属性name的值是name
		c.add(Restrictions.gt("id", 1));
		//c.setCacheable(true);
		list=c.list();*/
		//User u=(User)c.uniqueResult();
		
/*		Customer customer = customerDao.get(1);
		System.out.println(customer.getUsername());
		
		customer = customerDao.get(1);
		System.out.println(customer.getUsername());*/
		
		return JsonUtil.buildJson(EnumResponseType.SUCCESS, new TestDto(1, "name"));
	}
	
	@Transactional
	public byte[] doSomething2(@PassParameter(name = "id") int id){
		System.out.println("find the action, haha!");
		//userDao.excuteBySql("update user set password = '111', name = '111' where id = 1");
		User user = userDao.get(1);
		user.setName("xxx");
		user.setPassword("xxx");
		userDao.update(user);
		/*Criteria c = userDao.createCriteria();
		c.add(Restrictions.lt("id", 100));//属性name的值是name
		c.add(Restrictions.gt("id", 1));
		//c.setCacheable(true);
		List<User> list=c.list();
		
		c = userDao.createCriteria();
		c.add(Restrictions.lt("id", 100));//属性name的值是name
		c.add(Restrictions.gt("id", 1));
		//c.setCacheable(true);
		list=c.list();*/
		//User u=(User)c.uniqueResult();
		
/*		Customer customer = customerDao.get(1);
		System.out.println(customer.getUsername());
		
		customer = customerDao.get(1);
		System.out.println(customer.getUsername());*/
		
		return JsonUtil.buildJson(EnumResponseType.SUCCESS, new TestDto(1, "name"));
	}
}
