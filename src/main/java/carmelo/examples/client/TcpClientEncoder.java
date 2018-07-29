package carmelo.examples.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import carmelo.servlet.Request;

public class TcpClientEncoder extends MessageToByteEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Request request, ByteBuf out) throws Exception {
        
    	// encoding format: totalLength + requestId + commandLength + command + params
        int requestId = request.getId();
		String command = request.getCommand();
		String params = request.getParams();
		int commandLength = command.length();
		int totalLength = 8 + commandLength + params.length();
        out.writeInt(totalLength);
        out.writeInt(requestId);
        out.writeInt(commandLength);
        out.writeBytes(command.getBytes());
        out.writeBytes(params.getBytes());
    }
}
