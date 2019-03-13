package carmelo.hotupdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;


public class Attach {
	
	public static void main(String[] args) throws AttachNotSupportedException, IOException, AgentLoadException,
	AgentInitializationException, InterruptedException {
		String filePath = "updateconf" + File.separator + "pid";
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		String pid = br.readLine();
		br.close();
		VirtualMachine vm = VirtualMachine.attach(pid);
		String jarPath = "updateconf" + File.separator + "UpdateAgent.jar";
		vm.loadAgent(jarPath);
		vm.detach();
	}
}