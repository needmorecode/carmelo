package carmelo.hotupdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;


public class UpdateAgent {
	
	public static void agentmain(String args, Instrumentation inst) throws Exception {
		
		Class<?>[] allClass = inst.getAllLoadedClasses();
		String filePath = "updateconf" + File.separator + "class";
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		String className = br.readLine();
		String classFileName = "updateconf" + File.separator + br.readLine();
		br.close();
		
		for (Class<?> c : allClass) {
			if (c.getName().equals(className)) {
				String pathname = classFileName;
				File file = new File(pathname);
				try {
					byte[] bytes = fileToBytes(file);
					ClassDefinition classDefinition = new ClassDefinition(c, bytes);
					inst.redefineClasses(classDefinition);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static byte[] fileToBytes(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte[] bytes = new byte[in.available()];
		in.read(bytes);
		in.close();
		return bytes;
	}

}
