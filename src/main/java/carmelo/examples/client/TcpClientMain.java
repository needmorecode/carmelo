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
package carmelo.examples.client;

import java.util.Scanner;

import carmelo.common.Configuration;
import carmelo.servlet.Request;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;


/**
 * an tcp client example
 * 
 * @author needmorecode
 *
 */
public class TcpClientMain extends Thread{

    private final String host;
    private final int port;
    private final int firstMessageSize;
    private ChannelHandlerContext ctx;

    public TcpClientMain(String host, int port, int firstMessageSize) {
        this.host = host;
        this.port = port;
        this.firstMessageSize = firstMessageSize;
    }

    public void run() {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                	 ch.pipeline().addLast(new TcpClientDecoder()); 
                	 ch.pipeline().addLast(new TcpClientEncoder());
                	 ch.pipeline().addLast(new TcpClientHandler(firstMessageSize, TcpClientMain.this));
                 }
             });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } 
        catch (Exception e){
        	e.printStackTrace();
        }
        finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
        
        
    }
    
    public void sendMsg(int requestId, String command, String params){
    	Request request = new Request(requestId, command, params, "0", ctx);
    	ctx.write(request);
    	ctx.flush();
    	System.err.println("send request:" + requestId + " " + command + " " + params);
    }

    public static void main(String[] args) throws Exception {
        final String host = "127.0.0.1";
        final int port = Integer.parseInt(Configuration.getProperty(Configuration.TCP_PORT));;
        final int firstMessageSize;
        if (args.length == 3) {
            firstMessageSize = Integer.parseInt(args[2]);
        } else {
            firstMessageSize = 100;
        }

        TcpClientMain client = new TcpClientMain(host, port, firstMessageSize);
        client.start();
        System.err.println("try typing in following actions and have fun!\nuser!login name=1&password=123\nuser!logout\nuser!reconnect sessionId=2\n");
        Scanner scanner = new Scanner(System.in);
        int requestId = 1;
        while(true){
        	 String line = scanner.nextLine(); 
             if (line.equals("exit")) {
            	 System.out.println("TcpClient exit");
            	 System.exit(2);
            	 break; 
             }
             Scanner sc = new Scanner(line);
             String command = sc.next();
             String params = null;
             if (sc.hasNext())
            	 params = sc.next();
             else
            	 params = "";
             client.sendMsg(requestId, command, params);
             requestId++;
        }
    }

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
}
