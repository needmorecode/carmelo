package carmelo.proxool;

import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class ProxoolStartUp extends DriverManagerDataSource implements InitializingBean{

	public void afterPropertiesSet() throws Exception {
		try{
			String classpath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String proxoolPath = classpath + "proxool.xml";
			JAXPConfigurator.configure(proxoolPath, false);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
