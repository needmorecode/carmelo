package carmelo.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionManager {
	
	private final static SessionManager instance = new SessionManager();
	
	private final static AtomicInteger maxSessionId = new AtomicInteger();
	
	public static SessionManager getInstance(){
		return instance;
	}
	
	private SessionManager(){
		
	}
	
	private Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

	public Session getSession(String sessionId){
		Session session = sessionMap.get(sessionId);
		return session;
	}
	
	public Session createSession(){
		int currSessionId = maxSessionId.incrementAndGet();
		Session currSession = new Session(currSessionId + "");
		sessionMap.put(currSession.getSessionId(), currSession);
		return currSession;
	}
	
	public void destroySession(String sessionId){
		sessionMap.remove(sessionId);
	}

	
	
	
}
