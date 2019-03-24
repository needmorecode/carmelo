package carmelo.rpc;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSON;

import carmelo.examples.server.rpc.dto.TestReturnDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RpcManager {
	
	private static final RpcManager instance = new RpcManager();
	
	private AtomicInteger currReqId = new AtomicInteger();
	
	private Socket socket;
	
	public static RpcManager getInstance() {
		return instance;
	}
	
	public void init(String host, int port) {
		try {
			socket = new Socket(host, port);
			socket.setSoTimeout(3000);
			socket.setTcpNoDelay(true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Object invoke(String command, Map<String, Object> args, Class retClass) {
		try {
	        int requestId = currReqId.getAndIncrement();
			StringBuilder params = new StringBuilder();
			for (Entry<String, Object> entry : args.entrySet()) {
				String paramValue = JSON.toJSONString(entry.getValue());
				params.append(entry.getKey()).append("=").append(paramValue).append("&");
			}
			int commandLength = command.length();
			int totalLength = 8 + commandLength + params.length();
			ByteBuf bb = Unpooled.buffer();
			bb.writeInt(totalLength);
			bb.writeInt(requestId);
			bb.writeInt(commandLength);
			bb.writeBytes(command.getBytes());
			bb.writeBytes(params.toString().getBytes());
			
            byte[] bytes = new byte[bb.readableBytes()];
            bb.readBytes(bytes);
		 	
			socket.getOutputStream().write(bytes);
	        socket.getOutputStream().flush();
	        
	        // process response
	        byte[] lengthBytes = new byte[4];
	        socket.getInputStream().read(lengthBytes);
	        int length = getInt(lengthBytes);
	        byte[] contentBytes = new byte[length];
	        int offset = 0;
	        while (offset < length) {
	        	offset += socket.getInputStream().read(contentBytes, offset, length);
	        }
	        int readIndex = 0;
	        requestId = getInt(contentBytes);
	        readIndex += 4;
	        byte[] content = Arrays.copyOfRange(contentBytes, readIndex, contentBytes.length);
	        //System.out.println(new String(content));
	        return JSON.parseObject(new String(content), retClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private int getInt(byte[] bytes) {
		return (bytes[0] & 0xff) << 24
				| (bytes[1] & 0xff) << 16
				| (bytes[2] & 0xff) << 8
				| (bytes[3] & 0xff);
	}

}
