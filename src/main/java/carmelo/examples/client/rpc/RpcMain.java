package carmelo.examples.client.rpc;

import java.util.HashMap;
import java.util.Map;

import carmelo.common.Configuration;
import carmelo.examples.server.rpc.dto.TestArg1Dto;
import carmelo.examples.server.rpc.dto.TestArg2Dto;
import carmelo.examples.server.rpc.dto.TestReturnDto;
import carmelo.rpc.RpcManager;

public class RpcMain {
	
	public static void main(String args[]) {
        String host = "127.0.0.1";
        int port = Integer.parseInt(Configuration.getProperty(Configuration.TCP_PORT));
		RpcManager.getInstance().init(host, port);
		TestArg1Dto testArg1 = new TestArg1Dto(1);
		TestArg2Dto testArg2 = new TestArg2Dto(2);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("arg1", testArg1);
		params.put("arg2", testArg2);
		TestReturnDto ret = (TestReturnDto)RpcManager.getInstance().invoke("rpcTest!test", params, TestReturnDto.class);
		System.out.println(ret.getNum());
	}

}
