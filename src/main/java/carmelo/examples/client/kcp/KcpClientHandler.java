package carmelo.examples.client.kcp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


import carmelo.servlet.Request;
import io.jpower.kcp.netty.UkcpChannel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class KcpClientHandler extends ChannelInboundHandlerAdapter {

    private ScheduledExecutorService scheduleSrv;
    
    private static int requestId = 0;

    public KcpClientHandler() {
        scheduleSrv = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        UkcpChannel kcpCh = (UkcpChannel) ctx.channel();
        kcpCh.conv(10); // set conv

        scheduleSrv.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
            	
            	for (int i = 1; i <= 50; i++) {
            		requestId++;
            		String command = "user!register";
            		String params = "name=b" + requestId + "&password=123";
            		Request request = new Request(requestId, command, params, "0", ctx);
            		ctx.write(request);
            		ctx.flush();
            		System.err.println("send request:" + requestId + " " + command + " " + params);
            	}
            }
        }, 20, 20000, TimeUnit.MILLISECONDS);
    }

}

