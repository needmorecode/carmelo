package carmelo.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import carmelo.servlet.Response;

public class TcpEncoder extends MessageToByteEncoder<Response> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Response response, ByteBuf out) throws Exception {
        int responseId = response.getId();
        byte[] responseContents = response.getContents();
        int totalLength = 4 + responseContents.length;
    	out.writeInt(totalLength);
    	out.writeInt(responseId);
        out.writeBytes(responseContents);
    }
}
