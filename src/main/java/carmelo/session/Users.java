package carmelo.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
	
//	public static void push(Integer userId, String command) {
//		String sessionId = getSessionId(userId);
//		Session session = SessionManager.getInstance().getSession(sessionId);
//		session
//	}

}
