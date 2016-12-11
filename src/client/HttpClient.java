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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.ClientCookieEncoder;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;

/**
 * A simple HTTP client that prints out the content of the HTTP response to
 * {@link System#out} to test {@link HttpSnoopServer}.
 */
public class HttpClient {

    private final URI uri;

    public HttpClient(URI uri) {
        this.uri = uri;
    }

    public void run() throws Exception {
    	new Thread(){
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
			                         //new LoggingHandler(LogLevel.INFO),
			             }
			         });
			
			        
			        // Make the connection attempt.
			        Channel ch2;
					try {
						ch2 = b2.connect("127.0.0.1", 8037).sync().channel();
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
    	}.start();
    	
    	
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
                             //new LoggingHandler(LogLevel.INFO),
                 }
             });
            
            // Make the connection attempt.
            Channel ch = b.connect("127.0.0.1", 8035).sync().channel();

            // Wait for the server to close the connection.
            ch.closeFuture().sync();
            
        } finally {
            // Shut down executor threads to exit.
            group.shutdownGracefully();
        }
        
    }

    public static void main(String[] args) throws Exception {
/*        if (args.length != 1) {
            System.err.println(
                    "Usage: " + HttpSnoopClient.class.getSimpleName() +
                    " <URL>");
            return;
        }*/

        URI uri = new URI("127.0.0.1");
        new HttpClient(uri).run();
    }
}
