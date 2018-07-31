package carmelo.examples.server.system.action;

import org.springframework.stereotype.Component;

import carmelo.examples.server.login.dto.TestDto;
import carmelo.json.JsonUtil;
import carmelo.json.ResponseType;
import carmelo.servlet.Request;
import carmelo.servlet.annotation.PassParameter;
import carmelo.session.Session;
import carmelo.session.SessionConstants;
import carmelo.session.SessionManager;
import carmelo.session.Users;

@Component
public class HeartbeatAction {

	/**
	 * heartbeat
	 * @param request
	 * @return
	 */
	public byte[] heartbeat(Request request) {
		Session session = SessionManager.getInstance().getSession(request.getSessionId());
		session.access();
		return JsonUtil.buildJsonSuccess();
	}
}
