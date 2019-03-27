package carmelo.examples.client.websocket;

import carmelo.json.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
	
    private WebSocketClientHandshaker handshaker;
    
    private ChannelPromise handshakeFuture;
    
    private WebSocketClientMain client;
    
    public WebSocketClientHandler(WebSocketClientMain client) {
    	this.client = client;
    }
    
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }
    public WebSocketClientHandshaker getHandshaker() {
        return handshaker;
    }

    public void setHandshaker(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelPromise getHandshakeFuture() {
        return handshakeFuture;
    }

    public void setHandshakeFuture(ChannelPromise handshakeFuture) {
        this.handshakeFuture = handshakeFuture;
    }

    public ChannelFuture handshakeFuture() {
        return this.handshakeFuture;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    	client.setCtx(ctx);
    	handshaker.handshake(ctx.channel());
    }

    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        FullHttpResponse response;
        if (!this.handshaker.isHandshakeComplete()) {
            try {
                response = (FullHttpResponse)msg;
                this.handshaker.finishHandshake(ch, response);
                this.handshakeFuture.setSuccess();
                //System.out.println("WebSocket Client connected! response headers[sec-websocket-extensions]:{}"+response.headers());
            } catch (WebSocketHandshakeException var7) {
                var7.printStackTrace();
            	//FullHttpResponse res = (FullHttpResponse)msg;
                //String errorMsg = String.format("WebSocket Client failed to connect,status:%s,reason:%s", res.status(), res.content().toString(CharsetUtil.UTF_8));
                //this.handshakeFuture.setFailure(new Exception(errorMsg));
            }
        } else if (msg instanceof FullHttpResponse) {
            response = (FullHttpResponse)msg;
            throw new IllegalStateException("Unexpected FullHttpResponse");
            //this.listener.onFail(response.status().code(), response.content().toString(CharsetUtil.UTF_8));
            //throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        } else {
            WebSocketFrame frame = (WebSocketFrame)msg;
            if (frame instanceof TextWebSocketFrame) {
//                TextWebSocketFrame textFrame = (TextWebSocketFrame)frame;
//                String[] strArr = textFrame.text().split(" ");
//                int requestId = Integer.parseInt(strArr[0]);
//                String decStr = strArr[1];
//                System.err.println("receive reponse:" + requestId + " " + decStr);
            } else if (frame instanceof BinaryWebSocketFrame) {
                BinaryWebSocketFrame binFrame = (BinaryWebSocketFrame)frame;
                ByteBuf bb = binFrame.content();
                int requestId = bb.readInt();
                byte[] bytes = new byte[bb.readableBytes()];
                bb.readBytes(bytes);
                String decStr = JsonUtil.decompress(bytes);
                System.err.println("receive reponse:" + requestId + " " + decStr);
            } else if (frame instanceof PongWebSocketFrame) {
            } else if (frame instanceof CloseWebSocketFrame) {
                ch.close();
            }
        }
    }
}
