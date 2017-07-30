package carmelo.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
	
	public static final String USER_ID = "userId";
	
	private String sessionId;
	
	private Map<String, Object> params = new ConcurrentHashMap<String, Object>();

	public Session(String sessionId){
		this.sessionId = sessionId;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	
}
