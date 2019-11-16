package carmelo.netty.kcp;


import carmelo.servlet.Request;
import carmelo.servlet.Response;
import carmelo.servlet.Servlet;
import io.jpower.kcp.netty.UkcpChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class KcpServerHandler extends SimpleChannelInboundHandler<Request> {

	private Servlet servlet;
	
	public KcpServerHandler(Servlet servlet){
		this.servlet = servlet;
	}

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UkcpChannel kcpCh = (UkcpChannel) ctx.channel();
        kcpCh.conv(10);
    }

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
		Response response = servlet.service(request);
    	ctx.writeAndFlush(response);
	}

}

