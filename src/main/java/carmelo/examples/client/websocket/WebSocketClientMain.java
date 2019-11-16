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
package carmelo.examples.client.websocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

import carmelo.common.Configuration;
import carmelo.examples.client.tcp.TcpClientHandler;
import carmelo.examples.client.tcp.TcpClientMain;
import carmelo.servlet.Request;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * an websocket client example
 * 
 * @author needmorecode
 *
 */
public class WebSocketClientMain extends Thread {

	private final String host;
	private final int port;
	private ChannelHandlerContext ctx;

	public WebSocketClientMain(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() {
		// Configure the client.
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			WebSocketClientHandler handler = new WebSocketClientHandler(WebSocketClientMain.this);
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new HttpClientCodec(),
									new HttpObjectAggregator(8192),
									handler);
						}
					});
			String url = "ws://" + host + ":" + port + "/ws";
			URI websocketURI = new URI(url);
			HttpHeaders httpHeaders = new DefaultHttpHeaders();
			WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI,
					WebSocketVersion.V13, null, true, httpHeaders);
			handler.setHandshaker(handshaker);
			final Channel channel = b.connect(websocketURI.getHost(), websocketURI.getPort()).sync().channel();
			//handshaker.handshake(channel);
			handler.handshakeFuture().sync();
			channel.closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Shut down the event loop to terminate all threads.
			group.shutdownGracefully();
		}

	}

	public void sendMsg(int requestId, String command, String params) {
		Request request = new Request(requestId, command, params, "0", ctx);
		ctx.write(request);
		ctx.flush();
		System.err.println("send request:" + requestId + " " + command + " " + params);
	}

	public void sendMsg(int requestId, String msg) {
		TextWebSocketFrame frame = new TextWebSocketFrame(requestId + " " + msg);
		ctx.channel().writeAndFlush(frame);
		System.err.println("send request:" + msg);
	}

	public static void main(String[] args) throws Exception {
		final String host = "127.0.0.1";
		final int port = Integer.parseInt(Configuration.getProperty(Configuration.WEBSOCKET_PORT));
		;
		WebSocketClientMain client = new WebSocketClientMain(host, port);
		client.start();
		System.err.println(
				"try typing in following actions and have fun!\nuser!register name=1&password=123\nuser!login name=1&password=123\nuser!logout\nuser!reconnect sessionId=2\nuser!tryPush\n");
		Scanner scanner = new Scanner(System.in);
		int requestId = 1;
		while (true) {
			String line = scanner.nextLine();
			if (line.equals("exit")) {
				scanner.close();
				System.out.println("WebSocketClient exit");
				System.exit(2);
				break;
			}
			Scanner sc = new Scanner(line);
			client.sendMsg(requestId, line);
			requestId++;
			sc.close();
		}
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

}
