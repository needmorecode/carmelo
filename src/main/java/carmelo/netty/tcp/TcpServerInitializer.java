package carmelo.netty.tcp;

import carmelo.servlet.Servlet;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;

public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

	private Servlet servlet;
	
    public TcpServerInitializer(Servlet servlet){
    	this.servlet = servlet;
    }
    
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", new TcpDecoder());
        pipeline.addLast("encoder", new TcpEncoder());
        // and then business logic
        pipeline.addLast("handler", new TcpServerHandler(servlet));
    }
}
