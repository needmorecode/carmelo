package client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import servlet.Request;

public class TcpClientEncoder extends MessageToByteEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Request request, ByteBuf out) throws Exception {
        
        /*for (int i = 0; i < firstMessage.capacity(); i ++) {
            firstMessage.writeByte((byte) i);
        }*/
        // 编码格式：总长 + requestId + command长度 + command + params
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
    	
    	/*int responseId = response.getId();
        byte[] responseContents = response.getContents();
        int totalLength = 4 + responseContents.length;
    	out.writeInt(totalLength);
    	out.writeInt(responseId);
        out.writeBytes(responseContents);*/
    }
}
