package carmelo.netty.websocket;

import java.time.LocalDateTime;

import carmelo.servlet.Request;
import carmelo.servlet.Response;
import carmelo.servlet.Servlet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
	
	private Servlet servlet;
	
	public WebSocketServerHandler(Servlet servlet) {
		this.servlet = servlet;
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
    	
    	Request request = this.decode(msg.text(), ctx);
    	
				Response response = servlet.service(request);
				ByteBuf bb = encode(response);
		    	ctx.writeAndFlush(new BinaryWebSocketFrame(bb));
    	
    	
    	
    	
    	
    	
    }
    
    private Request decode(String msg, ChannelHandlerContext ctx) {
    	String[] strArr = msg.split(" ");
    	int requestId = Integer.parseInt(strArr[0]);
    	String command = strArr[1];
    	String params = strArr[2];
    	String sessionId = "";
    	return new Request(requestId, command, params, sessionId, ctx);
    }
    
    private ByteBuf encode(Response response) {
		ByteBuf bb = Unpooled.buffer();
		bb.writeInt(response.getId());
		bb.writeBytes(response.getContents());
		
        //byte[] bytes = new byte[bb.readableBytes()];
        //bb.readBytes(bytes);
    	
    	return bb;
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
