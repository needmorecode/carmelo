package carmelo.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	
	private static Properties prop;
	
	static{
		String classpath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String fileName = classpath + "server.properties";
        prop = new Properties();
        InputStream is;
		try {
			is = new FileInputStream(new File(fileName));
			prop.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	
	public static final String USER_NAME = "user_name";
	
	public static final String SCAN_ACTION_PACKAGE = "scan.action.package";
	
	public static final String TCP_PORT = "tcp.port";
	
	public static final String HTTP_PORT = "http.port";
	
	public static final String HTTP_PUSH_PORT = "http.push.port";
	
	/**
	 * get property
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return prop.getProperty(key);
	}
	
}
