package carmelo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * carmelo logger
 * 
 * @author needmorecode
 *
 */
public class CarmeloLogger {
	
	public static Logger INTERFACE = LoggerFactory.getLogger("interface");
	
	public static Logger ERROR = LoggerFactory.getLogger("error");
	
	public static Logger LOGIN = LoggerFactory.getLogger("login");
	
	public static Logger TIMER = LoggerFactory.getLogger("timer");
	
	
	public static void main(String args[]) {
		CarmeloLogger.INTERFACE.info("interface");
	}

}
