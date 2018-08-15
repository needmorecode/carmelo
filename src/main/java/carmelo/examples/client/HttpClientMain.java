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

import java.net.URI;

import carmelo.common.Configuration;
import carmelo.examples.client.HttpClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;


/**
 * an http client example
 * 
 * @author needmorecode
 *
 */
public class HttpClientMain {

    private final String host;

    public HttpClientMain(String host) {
        this.host = host;
    }

    public void run() throws Exception {
 /*   	new Thread(){
    		public void run() {
		    	Bootstrap b2 = new Bootstrap();
		    	EventLoopGroup group = new NioEventLoopGroup();
		    	try{
			        b2.group(group)
			         .channel(NioSocketChannel.class)
			         .handler(new ChannelInitializer<SocketChannel>() {
			             @Override
			             public void initChannel(SocketChannel ch) throws Exception {
			                 ch.pipeline().addLast(new HttpClientCodec());
			                 ch.pipeline().addLast(new HttpClientHandler());
			             }
			         });
			
			        
			        // Make the connection attempt.
			        Channel ch2;
					try {
						int port = Integer.parseInt(Configuration.getProperty(Configuration.HTTP_PUSH_PORT));
						ch2 = b2.connect(host, port).sync().channel();
						ch2.closeFuture().sync();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    	}
		        finally {
		            // Shut down executor threads to exit.
		            group.shutdownGracefully();
		        }
    		}
    	}.start();*/
    	
    	
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(new HttpClientCodec());
                     ch.pipeline().addLast(new HttpClientHandler());
                 }
             });
            
            // Make the connection attempt.
            int port = Integer.parseInt(Configuration.getProperty(Configuration.HTTP_PORT));
            Channel ch = b.connect(host, port).sync().channel();

            // Wait for the server to close the connection.
            ch.closeFuture().sync();
            
        } finally {
            // Shut down executor threads to exit.
            group.shutdownGracefully();
        }
        
    }

    public static void main(String[] args) throws Exception {
        new HttpClientMain("127.0.0.1").run();
    }
}
