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
import carmelo.servlet.Request;
import carmelo.servlet.annotation.PassParameter;
import carmelo.servlet.annotation.SessionParameter;
import carmelo.session.Session;
import carmelo.session.SessionConstants;
import carmelo.session.SessionManager;
import carmelo.session.Users;

/**
 * an example action which is used for a user to login, loginout, ...
 * 
 * @author needmorecode
 *
 */
@Component
public class UserAction {
	
	@Autowired
	private UserDao userDao;
	
	public byte[] login(@PassParameter(name = "name")String name, @PassParameter(name = "password")String password, Request request){
		int userId = 1;
		Session session = SessionManager.getInstance().createSession();
		session.getParams().put(SessionConstants.USER_ID, userId);
		String sessionId = session.getSessionId();
		Users.addUser(userId, sessionId);
		request.getCtx().attr(SessionConstants.SESSION_ID).set(sessionId);
		System.out.println("sessionId" + sessionId);
		return JsonUtil.buildJson(ResponseType.SUCCESS, new TestDto(1, "name"));
	}
	
	public byte[] logout(@SessionParameter(name = Session.USER_ID)int userId){
		String sessionId = Users.getSessionId(userId);
		SessionManager.getInstance().destroySession(sessionId);
		Users.removeUser(userId);
		return JsonUtil.buildJson(ResponseType.SUCCESS, new TestDto(1, "name"));
	}
	
	public byte[] reconnect(@PassParameter(name = "sessionId")String sessionId, Request request){
		Session session = SessionManager.getInstance().getSession(sessionId);
		if (session == null){
			System.out.println("reconnect fail");
			return JsonUtil.buildJson(ResponseType.FAIL, "");
		}
		request.getCtx().attr(SessionConstants.SESSION_ID).set(sessionId);
		System.out.println("reconnect success");
		return JsonUtil.buildJson(ResponseType.SUCCESS, new TestDto(1, "name"));
	}
	
	@Transactional
	public byte[] doSomething(@PassParameter(name = "id") int id){
		User user =userDao.get(1);
		userDao.getSession().evict(user);
		user = userDao.get(1);
		return JsonUtil.buildJson(ResponseType.SUCCESS, new TestDto(1, "name"));
	}
	
	@Transactional
	public byte[] doSomething2(@PassParameter(name = "id") int id){
		User user = userDao.get(1);
		user.setName("xxx");
		user.setPassword("xxx");
		userDao.update(user);
		return JsonUtil.buildJson(ResponseType.SUCCESS, new TestDto(1, "name"));
	}
}
