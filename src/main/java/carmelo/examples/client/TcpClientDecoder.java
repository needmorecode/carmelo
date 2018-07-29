package carmelo.examples.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import carmelo.servlet.Response;

public class TcpClientDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) {
		// wait until the length prefix is available
		if (in.readableBytes() < 4) {
			return;
		}
		in.markReaderIndex();

		// encoding format: totalLength + requestId + responseContent
		int totalLength = in.readInt();
		if (in.readableBytes() < totalLength) {
			in.resetReaderIndex();
			return;
		}

		int requestId = in.readInt();
		int contentLength = totalLength - 4;
		byte[] contentBytes = new byte[contentLength];
		in.readBytes(contentBytes);
		Response response = new Response(requestId, contentBytes);
		System.err.println("receive reponse:" + requestId + " " + new String(contentBytes));
		out.add(response);
	}
}
