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
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.net.JarURLConnection;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import carmelo.servlet.annotation.PassParameter;
import carmelo.servlet.annotation.SessionParameter;
import carmelo.session.Session;
import carmelo.session.SessionConstants;
import carmelo.session.SessionManager;
import carmelo.session.Users;
import carmelo.common.ClassUtil;
import carmelo.common.Configuration;

/**
 * 
 * a servlet which stores all actions and dispatch all requests to the right action
 * 
 * @author needmorecode
 *
 */
public class Servlet {
	
	private static Map<String, ActionInvocation> actionMap = new HashMap<String, ActionInvocation>();
	
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
		// TODO should return even if exception occurs
		try {
			ActionInvocation invocation = actionMap.get(request.getCommand());
			Object object = invocation.getObject();
			Method method = invocation.getMethod();
			List<Object> params = new LinkedList<Object>();
			for (int i = 0; i <= method.getParameterTypes().length - 1; i++){
				Class<?> paramType = method.getParameterTypes()[i];
				if (paramType.equals(Request.class))
					params.add(request);
				else{
					Annotation annotation = method.getParameterAnnotations()[i][0];
					if (annotation instanceof PassParameter){
					    String paramName = ((PassParameter)annotation).name();
					    String paramValue = request.getParamMap().get(paramName);
					    params.add(ClassUtil.stringToObject(paramValue, paramType));
					}
					else if (annotation instanceof SessionParameter){
						String paramName = ((SessionParameter)annotation).name();
						Attribute<String> attr = request.getCtx().channel().attr(SessionConstants.SESSION_ID);
						String sessionId = attr.get();
						// TODO should return if null
						Session session = SessionManager.getInstance().getSession(sessionId);
						Object paramValue = session.getParams().get(paramName);
						params.add(paramValue);
					}
				}
				
			}
		
			byte[] ret = (byte[])method.invoke(object, params.toArray());
			System.out.println("find method: " + method.getName());
			return new Response(request.getId(), ret);
		} catch (Exception e){
			e.printStackTrace();
			// TODO return exception state
			return new Response(request.getId(), null);
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
                                        	if (className.endsWith("Action"))
                                            classes.add(Class  
                                                    .forName(packageName + '.'  
                                                            + className));  
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
                	if (className.endsWith("Action"))
                		classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));    
                                } catch (ClassNotFoundException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  

}
