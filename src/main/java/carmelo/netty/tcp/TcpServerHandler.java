package carmelo.netty.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;

import carmelo.servlet.Request;
import carmelo.servlet.Response;
import carmelo.servlet.Servlet;

public class TcpServerHandler extends SimpleChannelInboundHandler<Request> {
	
	private Servlet servlet;
	
	public TcpServerHandler(Servlet servlet){
		this.servlet = servlet;
	}

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
    	Response response = servlet.service(request);
    	ctx.writeAndFlush(response);
    }


}
