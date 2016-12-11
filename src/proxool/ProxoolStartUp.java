package proxool;

import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class ProxoolStartUp extends DriverManagerDataSource implements InitializingBean{

	@Override
	public void afterPropertiesSet() throws Exception {
		try{
			JAXPConfigurator.configure("E:\\workspace\\java\\medicine-server\\src\\proxool.xml", false); 
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
