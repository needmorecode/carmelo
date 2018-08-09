package carmelo.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import carmelo.servlet.Response;

/**
 * all online users
 * 
 * @author needmorecode
 *
 */
public class Users {
	
	private static Map<Integer, String> uid2sidMap = new ConcurrentHashMap<Integer, String>();
	private static Map<String, Integer> sid2uidMap = new ConcurrentHashMap<String, Integer>();
	
	public static Integer getUserId(String sessionId){
		return sid2uidMap.get(sessionId);
	}
	
	public static String getSessionId(Integer userId){
		return uid2sidMap.get(userId);
	}
	
	public static void addUser(Integer userId, String sessionId){
		sid2uidMap.put(sessionId, userId);
		uid2sidMap.put(userId, sessionId);
	}
	
	public static void removeUser(Integer userId){
		String sessionId = uid2sidMap.remove(userId);
		sid2uidMap.remove(sessionId);
	}
	
	public static void push(Integer userId, byte[] content) {
		String sessionId = getSessionId(userId);
		if (sessionId != null) {
			Session session = SessionManager.getInstance().getSession(sessionId);
			if (session != null) {
				Response response = new Response(0, content);
				session.push(response);
			}
		}
	}

}
