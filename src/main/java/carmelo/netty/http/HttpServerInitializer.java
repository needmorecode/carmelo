package carmelo.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import carmelo.netty.tcp.TcpDecoder;
import carmelo.netty.tcp.TcpEncoder;
import carmelo.netty.tcp.TcpServerHandler;
import carmelo.servlet.Servlet;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

	//private HttpServerHandler serverHandler;
	
	private Servlet servlet;
	
    public HttpServerInitializer(Servlet servlet){
    	this.servlet = servlet;
    }
    
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));

        // and then business logic.
        pipeline.addLast("handler", new HttpServerHandler(servlet));
    }
}
