/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.util.logging.Level;
import java.util.logging.Logger;

import server.login.dto.TestDto;
import servlet.Request;
import servlet.Response;
import session.Session;
import session.SessionManager;
import json.ResponseDto;

import com.alibaba.fastjson.JSON;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class TcpClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(
            TcpClientHandler.class.getName());
    
    private ByteBuf firstMessage;
    
    private int bufSize = 0;
    
    private TcpClient tcpClient;

    /**
     * Creates a client-side handler.
     */
    public TcpClientHandler(int firstMessageSize, TcpClient tcpClient) {
        if (firstMessageSize <= 0) {
            throw new IllegalArgumentException("firstMessageSize: " + firstMessageSize);
        }
        bufSize = firstMessageSize;
        this.tcpClient = tcpClient;
/*        firstMessage = Unpooled.buffer(firstMessageSize);
            firstMessage.writeByte((byte) i);
        }*/
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    	tcpClient.setCtx(ctx);
    	/*Session session = SessionManager.getInstance().getValidSession(0 + "");
    	String sessionId = session.getSessionId();
    	ctx.attr(SESSION_ID).set(sessionId);*/
    			
    	
    	//for (int j = 1; j  <= 5;  j++){
    		// 一开始的capacity是100，可以按需动态扩展capacity
            /*firstMessage = Unpooled.buffer(bufSize);
            for (int i = 0; i < firstMessage.capacity(); i ++) {
                firstMessage.writeByte((byte) i);
            }
            // 编码格式：总长 + requestId + command长度 + command + params
            int requestId = 1;
			String command = "user!doSomething";
			String params = "id=1";
			int commandLength = command.length();
			int totalLength = 8 + commandLength + params.length();
            firstMessage.writeInt(totalLength);
            firstMessage.writeInt(requestId);
            firstMessage.writeInt(commandLength);
            firstMessage.writeBytes(command.getBytes());
            firstMessage.writeBytes(params.getBytes());
    		ctx.writeAndFlush(firstMessage);*/
    	//}
    	/*firstMessage = Unpooled.buffer(bufSize);
    	ctx.writeAndFlush(firstMessage);*/
    	
/*    	Request request = new Request(0, "user!doSomething","id=1", "0", ctx);
    	ctx.write(request);
    	ctx.flush();*/
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
    	if (msg instanceof Response){
    		/*// TODO
    		Response response = (Response)msg;
    		ResponseDto responseDto = JSON.parseObject(new String(response.getContents()), ResponseDto.class);
    		TestDto testDto = JSON.parseObject(responseDto.getData().toString(), TestDto.class);
    		System.out.println();
    		
    		new Thread(){
    			public void run(){
    				try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    		    	Request request = new Request(0, "user!login","name=1&password=123", "0", ctx);
    		    	ctx.write(request);
    		    	ctx.flush();
    			}
    		}.start();*/
    	}
    	//ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        logger.log(Level.WARNING, "Unexpected exception from downstream.", cause);
        ctx.close();
    }
}
