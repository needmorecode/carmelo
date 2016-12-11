import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

import com.alibaba.fastjson.serializer.JSONSerializer;

import netty.MyBootstrap;



public class Main {
	
	public static void main(String args[]){
		/*try {
			JAXPConfigurator.configure("E:\\workspace\\java\\medicine-server\\src\\proxool.xml", false);
		} catch (ProxoolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		
		new MyBootstrap().run();
	}

}
