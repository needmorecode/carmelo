package carmelo.examples.server.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import carmelo.examples.server.login.dao.UserDao;
import carmelo.examples.server.login.domain.User;
import carmelo.examples.server.login.dto.TestDto;
import carmelo.json.JsonBuilder;
import carmelo.json.JsonUtil;
import carmelo.json.ResponseType;
import carmelo.servlet.Request;
import carmelo.session.Session;
import carmelo.session.SessionConstants;
import carmelo.session.SessionManager;
import carmelo.session.Users;

@Component
public class UserService {

	@Autowired
	private UserDao userDao;
	
	/**
	 * register
	 * @param name
	 * @param password
	 * @return
	 */
	@Transactional
	public byte[] register(String name, String password) {
		User user = userDao.getUser(name);
		if (user != null)
			return JsonUtil.buildJson(ResponseType.FAIL, "already registered");
		
		user = new User();
		user.setName(name);
		user.setPassword(password);
		userDao.save(user);
		
		return JsonUtil.buildJson(ResponseType.SUCCESS, "");
	}
	
	/**
	 * login
	 * @param name
	 * @param password
	 * @param request
	 * @return
	 */
	public byte[] login(String name, String password, Request request) {
		User user = userDao.getUser(name);
		if (user == null)
			return JsonUtil.buildJson(ResponseType.FAIL, "user not exists");
		if (!user.getPassword().equals(password))
			return JsonUtil.buildJson(ResponseType.FAIL, "wrong password");
		
		int userId = user.getId();
		Session session = SessionManager.getInstance().createSession();
		session.getParams().put(SessionConstants.USER_ID, userId);
		String sessionId = session.getSessionId();
		Users.addUser(userId, sessionId);
		request.getCtx().attr(SessionConstants.SESSION_ID).set(sessionId);
		System.out.println("sessionId: " + sessionId);
		
		JsonBuilder builder = new JsonBuilder();
		builder.startObject();
		builder.writeKey("sessionId");
		builder.writeValue(sessionId);
		builder.endObject();
		return JsonUtil.buildJson(ResponseType.SUCCESS, builder.toString());
	}
	
	/**
	 * logout
	 * @param userId
	 * @return
	 */
	public byte[] logout(int userId){
		String sessionId = Users.getSessionId(userId);
		if (sessionId == null)
			return JsonUtil.buildJson(ResponseType.FAIL, "already offline");
		
		SessionManager.getInstance().destroySession(sessionId);
		Users.removeUser(userId);
		
		return JsonUtil.buildJson(ResponseType.SUCCESS, "");
	}
	
	/**
	 * reconnect
	 * @param sessionId
	 * @param request
	 * @return
	 */
	public byte[] reconnect(String sessionId, Request request){
		Session session = SessionManager.getInstance().getSession(sessionId);
		if (session == null){
			System.out.println("reconnect fail");
			return JsonUtil.buildJson(ResponseType.FAIL, "reconnect fail");
		}
		request.getCtx().attr(SessionConstants.SESSION_ID).set(sessionId);
		System.out.println("reconnect success");
		return JsonUtil.buildJson(ResponseType.SUCCESS, "");
	}
	
	@Transactional
	public byte[] doSomething(int id){
//		User user =userDao.get(1);
//		userDao.getSession().evict(user);
//		user = userDao.get(1);
		return JsonUtil.buildJson(ResponseType.SUCCESS, "");
	}
	
	@Transactional
	public byte[] doSomething2(int id){
//		User user = userDao.get(1);
//		user.setName("xxx");
//		user.setPassword("xxx");
//		userDao.update(user);
		return JsonUtil.buildJson(ResponseType.SUCCESS, "");
	}
}
