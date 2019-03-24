package carmelo.netty.websocket;

import carmelo.netty.http.HttpServerHandler;
import carmelo.servlet.Servlet;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

	private Servlet servlet;
	
    public WebSocketServerInitializer(Servlet servlet){
    	this.servlet = servlet;
    }
    
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("codec", new HttpServerCodec());
        pipeline.addLast("chunked", new ChunkedWriteHandler());
        pipeline.addLast("aggregator", new HttpObjectAggregator(8192));
        pipeline.addLast("protocol", new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast("handler", new WebSocketServerHandler(servlet));
    }
}
