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
	
	// 是否走推送方式
	private boolean isChunked;
	
    public HttpServerInitializer(Servlet servlet, boolean isChunked){
    	this.servlet = servlet;
    	this.isChunked = isChunked;
    }
    
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        if (isChunked)
        	pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

        // and then business logic.
        if (isChunked)
        	pipeline.addLast("handler", new HttpServerChunkedHandler());
        else
        	pipeline.addLast("handler", new HttpServerHandler(servlet));
    }
}
