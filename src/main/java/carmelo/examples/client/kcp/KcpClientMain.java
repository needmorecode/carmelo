package carmelo.examples.client.kcp;

import carmelo.common.Configuration;
import carmelo.examples.client.codec.ClientDecoder;
import carmelo.examples.client.codec.ClientEncoder;
import io.jpower.kcp.netty.ChannelOptionHelper;
import io.jpower.kcp.netty.UkcpChannel;
import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpClientChannel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class KcpClientMain {
	
    public static void main(String[] args) throws Exception {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(UkcpClientChannel.class)
                    .handler(new ChannelInitializer<UkcpChannel>() {
                        @Override
                        public void initChannel(UkcpChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                       	 	p.addLast(new ClientDecoder()); 
                       	 	p.addLast(new ClientEncoder());
                            p.addLast(new KcpClientHandler());
                        }
                    });
            ChannelOptionHelper.nodelay(b, true, 20, 2, true)
                    .option(UkcpChannelOption.UKCP_MTU, 512);

            // Start the client.
            int port = Integer.parseInt(Configuration.getProperty(Configuration.KCP_PORT));
            ChannelFuture f = b.connect("127.0.0.1", port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

}
