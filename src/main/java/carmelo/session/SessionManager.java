package carmelo.session;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
	
	private final static SessionManager instance = new SessionManager();
	
	public static final int RANDOM_BIT_NUM = 7;
	
	public static SessionManager getInstance(){
		return instance;
	}
	
	private SessionManager(){
		initThread();
	}
	
	private Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

	public Session getSession(String sessionId){
		Session session = sessionMap.get(sessionId);
		return session;
	}
	
	public Session createSession(){
		StringBuilder sessionIdBuilder = new StringBuilder();
		for (int i = 1; i <= RANDOM_BIT_NUM; i++) {
			int randomNum = new Random().nextInt(10 + 26 + 26);
			char c;
			if (randomNum <= 9)
				c = (char) ('0' + randomNum);
			else if (randomNum <= 35)
				c = (char) ('A' + randomNum - 10);
			else
				c = (char) ('a' + randomNum - 36);
			sessionIdBuilder.append(c);
		}
		sessionIdBuilder.append(System.currentTimeMillis());
		String sessionId = sessionIdBuilder.toString();
		Session currSession = new Session(sessionId);
		sessionMap.put(currSession.getSessionId(), currSession);
		return currSession;
	}
	
	public void destroySession(String sessionId){
		sessionMap.remove(sessionId);
	}

	public void initThread() {
		new Thread() {
			public void run() {
				while (true) {
					for (Session session : sessionMap.values()) {
						try {
							if (session.isTimeout())
							{
								destroySession(session.getSessionId());
								Users.removeUser((Integer)(session.getParams().get(SessionConstants.USER_ID)));
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	
}
