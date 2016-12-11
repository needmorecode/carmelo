package servlet;

import java.lang.reflect.Method;

public class ActionInvocation {
	
	private String actionName;
	
	private Object object;
	
	private Method method;
	
	public ActionInvocation(Object object, Method method){
		this.object = object;
		this.method = method;
		String className = object.getClass().getSimpleName();
		className = (char)(className.charAt(0) - ('A' - 'a')) + className.substring(1, className.indexOf("Action"));
		String methodName = method.getName();
		this.setActionName(className + "!" + methodName);
	}
	
	public static void main(String args[]){
		System.out.println((char)('u' - 10));
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	

}
