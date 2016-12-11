package netty.http;

import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.COOKIE;
import static io.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import servlet.Request;
import servlet.Response;
import servlet.Servlet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.ServerCookieEncoder;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

	private Servlet servlet;
	
    private HttpRequest currHttpRequest;
    
    private String command;
    
    private String params;
	
	public HttpServerHandler(Servlet servlet){
		this.servlet = servlet;
	}
	
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            this.currHttpRequest = httpRequest;
           /* // 解析头部各种属性
            if (is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }*/
            //boolean keepAlive = isKeepAlive(httpRequest);
            //String temp = "http://127.0.0.1:8034/command=user!doSomething?id=1";
            //int requestId = 0;
            if (httpRequest.getMethod().equals(HttpMethod.GET)){
            	Pattern p = Pattern.compile(".*/command=(.*)\\?(.*)"); 
            	Matcher m = p.matcher(httpRequest.getUri()); 
            	if (m.matches()){
            		command = m.group(1);
            		params = m.group(2);
            	}
            }
            else if (httpRequest.getMethod().equals(HttpMethod.POST)){
            	Pattern p = Pattern.compile("http://(.*):(.*)/command=(.*)"); 
            	Matcher m = p.matcher(httpRequest.getUri()); 
            	if (m.matches()){
            		command = m.group(3);
            	}
            }
           /* else if (req.getMethod().equals(HttpMethod.POST)){
            	Pattern p = Pattern.compile("http://(.*):(.*)/command=(.*)"); 
            	Matcher m = p.matcher(req.getUri()); 
            	if (m.matches()){
            		command = m.group(3);
            		params = req.
            	}
            }*/
  
        }
        
        if (msg instanceof HttpContent) {
        	HttpContent httpContent = (HttpContent) msg;
        	if (currHttpRequest.getMethod().equals(HttpMethod.POST)){
        		params = httpContent.content().copy().toString();
        	}
        	
            Request request = new Request(0, command, params, "0", ctx);
            Response response = servlet.service(request);
            
            ByteBuf responseBuf = Unpooled.wrappedBuffer(response.getContents());
            
            
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, responseBuf);
            httpResponse.headers().set(CONTENT_TYPE, "text/plain");
            httpResponse.headers().set(CONTENT_LENGTH, httpResponse.content().readableBytes());

            boolean keepAlive = isKeepAlive(currHttpRequest);
            if (!keepAlive) {
                ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
            } else {
                httpResponse.headers().set(CONNECTION, Values.KEEP_ALIVE);
                ctx.write(httpResponse);
            }
            /*HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            boolean keepAlive = isKeepAlive(currRequest);
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1, currentObj.getDecoderResult().isSuccess()? OK : BAD_REQUEST,
                    Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));

            response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

            if (keepAlive) {
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);*/
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
