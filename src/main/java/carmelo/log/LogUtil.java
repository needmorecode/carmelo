package carmelo.log;

import carmelo.timer.TimerTask;

/**
 * log utility
 * 
 * @author needmorecode
 *
 */
public class LogUtil {
	
	/**
	 * build interface log
	 * @param userId
	 * @param actionName
	 * @param methodName
	 * @param command
	 * @param paramsBuilder
	 * @return
	 */
	public static String buildInterfaceLog(Integer userId, String actionName, String methodName, String command, StringBuilder paramsBuilder) {
		StringBuilder builder = new StringBuilder();
		builder.append(userId).append("#").append(actionName).append("#").append(methodName).append("#").append(command).append("#").append(paramsBuilder);
		return builder.toString();
	}
	
	/**
	 * build exception log
	 * @param userId
	 * @param actionName
	 * @param methodName
	 * @param command
	 * @return
	 */
	public static String buildExceptionLog(Integer userId, String actionName, String methodName, String command) {
		StringBuilder builder = new StringBuilder();
		builder.append(userId).append("#").append(actionName).append("#").append(methodName).append("#").append(command).append("#");
		return builder.toString();
	}
	
	/**
	 * build login log
	 * @param userId
	 * @return
	 */
	public static String buildLoginLog(Integer userId) {
		StringBuilder builder = new StringBuilder();
		builder.append("login").append("#").append(userId);
		return builder.toString();
	}
	
	/**
	 * build logout log
	 * @param userId
	 * @return
	 */
	public static String buildLogoutLog(Integer userId) {
		StringBuilder builder = new StringBuilder();
		builder.append("logout").append("#").append(userId);
		return builder.toString();
	}
	
	public static String buildTimerScheduleLog(TimerTask task) {
		StringBuilder builder = new StringBuilder();
		builder.append("schedule").append("#").append(task.getTaskId()).append("#").append(task.getName()).append("#").append(task.getExecTime());
		return builder.toString();
	}
	
	public static String buildTimerCancelLog(TimerTask task) {
		StringBuilder builder = new StringBuilder();
		builder.append("cancel").append("#").append(task.getTaskId()).append("#").append(task.getName()).append("#").append(task.getExecTime());
		return builder.toString();
	}
	
	public static String buildTimerStartLog(TimerTask task) {
		StringBuilder builder = new StringBuilder();
		builder.append("start").append("#").append(task.getTaskId()).append("#").append(task.getName()).append("#").append(System.currentTimeMillis());
		return builder.toString();
	}
	
	public static String buildTimerEndLog(TimerTask task) {
		StringBuilder builder = new StringBuilder();
		builder.append("end").append("#").append(task.getTaskId()).append("#").append(task.getName()).append("#").append(System.currentTimeMillis());
		return builder.toString();
	}

}
