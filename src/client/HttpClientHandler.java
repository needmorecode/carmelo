package client;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.ClientCookieEncoder;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.http.ClientCookieEncoder;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ByteBuf bb = null;
		try {
			bb = Unpooled.wrappedBuffer(new String("id=1").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// post是否能够这么写？参考HttpUploadClient TODO
		HttpRequest request = new DefaultFullHttpRequest(
	            HttpVersion.HTTP_1_1, HttpMethod.GET, "http://127.0.0.1:8034/command=user!doSomething?id=1");
	    //System.out.println(uri.getRawPath());
	    // some headers
	    request.headers().set(HttpHeaders.Names.HOST, 8043);
	    // 1.1默认持久连接，非持久连接要加CLOSE
	    // 1.0默认非持久连接，持久连接要加Keep-Alive
	    //request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
	    // 这个是干嘛的？表示客户端支持的编码（因为客户端服务端编解码都走netty提供的api了，所以这里加不加无所谓）
	    request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);

	    // Set some example cookies.
	    // some cookies
	    // cookie is also a part of headers
	   /* request.headers().set(
	            HttpHeaders.Names.COOKIE,
	            ClientCookieEncoder.encode(
	                    new DefaultCookie("my-cookie", "foo"),
	                    new DefaultCookie("another-cookie", "bar")));*/

	    // Send the HTTP request.
	    ctx.writeAndFlush(request);
	}
	
	
    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
    	
    	// 为何会读到两次？因为解码时会分别解析成HttpMessage和HttpContent
        if (msg instanceof HttpResponse) {
        	System.out.println("HttpResponse#######################");
            HttpResponse response = (HttpResponse) msg;

            // 状态码：200
            System.out.println("STATUS: " + response.getStatus());
            // 协议版本：1.1
            System.out.println("VERSION: " + response.getProtocolVersion());
            System.out.println();

            // 头部各种参数解析
            if (!response.headers().isEmpty()) {
                for (String name: response.headers().names()) {
                    for (String value: response.headers().getAll(name)) {
                        System.out.println("HEADER: " + name + " = " + value);
                    }
                }
                System.out.println();
            }

            // 这边有对chunked的处理，可具体研究一下
            if (HttpHeaders.isTransferEncodingChunked(response)) {
                System.out.println("CHUNKED CONTENT {");
            } else {
                System.out.println("CONTENT {");
            }
        }
        if (msg instanceof HttpContent) {
        	System.out.println("HttpContent#######################");
            HttpContent content = (HttpContent) msg;

            System.out.print(content.content().toString(CharsetUtil.UTF_8));
            System.out.flush();

            if (content instanceof LastHttpContent) {
                System.out.println("} END OF CONTENT");
            }
        }
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
