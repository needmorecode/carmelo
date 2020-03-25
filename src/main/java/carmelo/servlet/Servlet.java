package carmelo.servlet;


import io.netty.util.Attribute;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.net.JarURLConnection;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;

import carmelo.servlet.annotation.PassParameter;
import carmelo.servlet.annotation.SessionParameter;
import carmelo.session.Session;
import carmelo.session.SessionConstants;
import carmelo.session.SessionManager;
import carmelo.session.Users;
import carmelo.common.ClassUtil;
import carmelo.common.Configuration;
import carmelo.json.JsonUtil;
import carmelo.log.CarmeloLogger;
import carmelo.log.LogUtil;

/**
 * 
 * a servlet which stores all actions and dispatch all requests to the right action
 * 
 * @author needmorecode
 *
 */
public class Servlet {
	
	private static Map<String, ActionInvocation> actionMap = new HashMap<String, ActionInvocation>();
	
	private ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 2000, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(5));
	
	/**
	 * initiation
	 */
	public void init(){
		String scanActionPackage = Configuration.getProperty(Configuration.SCAN_ACTION_PACKAGE);
		Set<Class<?>> classes = getActionClasses(scanActionPackage);
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("applicationContext.xml");
		for (Class<?> clazz : classes){
			// get objects from spring bean factory
			Object object = beanFactory.getBean(clazz);
			Method[] methods = clazz.getDeclaredMethods();
			for (Method method : methods){
				ActionInvocation actionInvocation = new ActionInvocation(object, method);
				actionMap.put(actionInvocation.getActionName(), actionInvocation);
			}
		}
	}
	
	/**
	 * dispatch the request to the right action
	 * @param request
	 * @return
	 */
	public Response service(Request request){
		Method method = null;
		Session session = null;
		ActionInvocation invocation = null;
		try {
			invocation = actionMap.get(request.getCommand());
			boolean isRpc = isRpc(invocation.getActionName());
			Object object = invocation.getObject();
			method = invocation.getMethod();
			List<Object> params = new LinkedList<Object>();
			StringBuilder paramsBuilder = new StringBuilder();
			
			session = this.getSessionFromRequest(request);
			
			for (int i = 0; i <= method.getParameterTypes().length - 1; i++){
				Class<?> paramType = method.getParameterTypes()[i];
				if (paramType.equals(Request.class))
					params.add(request);
				else{
					Annotation annotation = method.getParameterAnnotations()[i][0];
					if (annotation instanceof PassParameter){
					    String paramName = ((PassParameter)annotation).name();
					    String paramValue = request.getParamMap().get(paramName);
					    if (!isRpc) {
					    	params.add(ClassUtil.stringToObject(paramValue, paramType));
					    }
					    else {
					    	params.add(JSON.parseObject(paramValue, paramType));
					    }
					    if (paramsBuilder.length() != 0) {
							paramsBuilder.append("&");
						}
						paramsBuilder.append(paramName).append("=").append(paramValue);
					}
					else if (annotation instanceof SessionParameter){
						String paramName = ((SessionParameter)annotation).name();
						if (session == null) {
							return new Response(request.getId(), JsonUtil.buildJsonUnlogin());
						}
						Object paramValue = session.getParams().get(paramName);
						params.add(paramValue);
					}
				}
				
			}

			Object retObj = method.invoke(object, params.toArray());
			byte[] ret = null;
			if (isRpc) {
				ret = JSON.toJSONString(retObj).getBytes();
			}
			else {
				ret = (byte[])retObj;
			}
			//byte[] ret = (byte[])method.invoke(object, params.toArray());
			
			session = this.getSessionFromRequest(request);
			
			Integer userId = session != null ? (Integer)session.getParams().get(SessionConstants.USER_ID) : null;
			String interfaceLog = LogUtil.buildInterfaceLog(userId, invocation.getActionName(), invocation.getMethod().getName(), request.getCommand(), paramsBuilder);
			CarmeloLogger.INTERFACE.info(interfaceLog);
			return new Response(request.getId(), ret);
		} catch (Exception e){
			Integer userId = session != null ? (Integer)session.getParams().get(SessionConstants.USER_ID) : null;
			String exceptionLog = LogUtil.buildExceptionLog(userId, invocation != null ? invocation.getActionName() : null, invocation != null ? invocation.getMethod().getName() : null, request.getCommand());
			CarmeloLogger.ERROR.error(exceptionLog, e);
			return new Response(request.getId(), JsonUtil.buildJsonException());
		}
	}
	
	public void destroy(){

	}
	
	/** 
	 * get all action classes in the destination package
     * @param pack
     * @return 
     */  
    public static Set<Class<?>> getActionClasses(String pack) {  
  
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();  
        boolean recursive = true;  
        String packageName = pack;  
        String packageDirName = packageName.replace('.', '/');  
        Enumeration<URL> dirs;  
        try {  
        	// get the classLoader from the current thread
            dirs = Thread.currentThread().getContextClassLoader().getResources(  
                    packageDirName);  
            while (dirs.hasMoreElements()) {  
                URL url = dirs.nextElement();  
                // get the protocol name  
                String protocol = url.getProtocol();  
                if ("file".equals(protocol)) {  
                	// if it is a file
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");  
                    // scan all files in the package 
                    findAndAddClassesInPackageByFile(packageName, filePath,  
                            recursive, classes);  
                } else if ("jar".equals(protocol)) {  
                    // if it is a jar file 
                    JarFile jar;  
                    try {  
                        jar = ((JarURLConnection) url.openConnection())  
                                .getJarFile();  
                        // iterate all entries in the jar
                        Enumeration<JarEntry> entries = jar.entries();  
                        while (entries.hasMoreElements()) {  
                            JarEntry entry = entries.nextElement();  
                            String name = entry.getName();  
                            if (name.charAt(0) == '/') {  
                                name = name.substring(1);  
                            }  
                            if (name.startsWith(packageDirName)) {  
                                int idx = name.lastIndexOf('/');  
                                if (idx != -1) {  
                                    // transfer "/" to "."  
                                    packageName = name.substring(0, idx)  
                                            .replace('/', '.');  
                                }  
                                if ((idx != -1) || recursive) {  
                                    if (name.endsWith(".class")  
                                            && !entry.isDirectory()) {  
                                        // remove .class suffix
                                        String className = name.substring(  
                                                packageName.length() + 1, name  
                                                        .length() - 6);  
                                        try {  
                                            // if it ends with Action
                                        	if (className.endsWith("Action")) {
                                        		classes.add(Class  
                                        				.forName(packageName + '.'  
                                        						+ className)); 
                                        	}
                                        } catch (ClassNotFoundException e) {  
                                            e.printStackTrace();  
                                        }  
                                    }  
                                }  
                            }  
                        }  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        return classes;  
    }  
    
    /** 
     * find all classes in the destination directory 
     *  
     * @param packageName 
     * @param packagePath 
     * @param recursive 
     * @param classes 
     */  
    public static void findAndAddClassesInPackageByFile(String packageName,  
            String packagePath, final boolean recursive, Set<Class<?>> classes) {  
        File dir = new File(packagePath);  
        if (!dir.exists() || !dir.isDirectory()) {  
            return;  
        }  
        File[] dirfiles = dir.listFiles(new FileFilter() {  
            // define the filter policy: ending with .class  
            @Override
            public boolean accept(File file) {  
                return (recursive && file.isDirectory())  
                        || (file.getName().endsWith(".class"));  
            }  
        });  
        for (File file : dirfiles) {  
            // if it is a directory, continue scanning
            if (file.isDirectory()) {  
                findAndAddClassesInPackageByFile(packageName + "."  
                        + file.getName(), file.getAbsolutePath(), recursive,  
                        classes);  
            } else {  
                // if it is a .class file, remove its file suffix
                String className = file.getName().substring(0,  
                        file.getName().length() - 6);  
                try {  
                    // if ending with Action, put it into the set
                	if (className.endsWith("Action")) {
                		classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));    
                	}
                } catch (ClassNotFoundException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }

	public ThreadPoolExecutor getExecutor() {
		return executor;
	}
	
	/**
	 * get session from request
	 * @param request
	 * @return
	 */
	private Session getSessionFromRequest(Request request) {
		Attribute<String> attr = request.getCtx().channel().attr(SessionConstants.SESSION_ID);
		if (attr != null) {
			String sessionId = attr.get();
			if (sessionId != null) {
				return SessionManager.getInstance().getSession(sessionId);
			}
		}
		return null;
	}
	
	/**
	 * check if it is rpc action
	 * @param actionName
	 * @return
	 */
	private boolean isRpc(String actionName) {
		return actionName.startsWith("rpc");
	}

}
