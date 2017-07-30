package carmelo.examples.server;
import java.io.IOException;

import carmelo.netty.GameServerBootstrap;

public class ServerMain {
	
	public static void main(String args[]) throws IOException{
		new GameServerBootstrap().run();
	}

}
