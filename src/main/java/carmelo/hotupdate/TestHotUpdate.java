package carmelo.hotupdate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * 
 * Try this hot update demo. First start TestHotUpdate, then start Attach, at last you will see the TestHotUpdate.class has been updated.
 * @author needmorecode
 *
 */
public class TestHotUpdate {
	
	public static void main(String args[]) throws IOException {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		String pid = name.split("@")[0];
		System.out.println("pid: " + pid);
		String filePath = "updateconf" + File.separator + "pid";
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println(file.getAbsolutePath());
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file);
		fw.write(pid);
		fw.close();
		
		TestHotUpdate update = new TestHotUpdate();
		update.test();
		try {
			Thread.sleep(12000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		update.test();
	}
	
	public void test() {
		System.out.println("before update");
	}

}
