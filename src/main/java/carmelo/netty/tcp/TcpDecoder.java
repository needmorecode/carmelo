package carmelo.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.List;

import carmelo.servlet.Request;

public class TcpDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) {
		// wait until the length prefix is available
		if (in.readableBytes() < 4) {
			return;
		}
		in.markReaderIndex();

		// encoding format: totalLength + requestId + commandLength + command + params
		int totalLength = in.readInt();
		if (in.readableBytes() < totalLength) {
			in.resetReaderIndex();
			return;
		}

		int requestId = in.readInt();
		int commandLength = in.readInt();
		byte[] commandBytes = new byte[commandLength];
		byte[] paramsBytes = new byte[totalLength - 8 - commandLength];
		in.readBytes(commandBytes);
		in.readBytes(paramsBytes);
		String command = new String(commandBytes);
		String params = new String(paramsBytes);
		Request request = new Request(requestId, command, params, "0", ctx);
		out.add(request);
	}
}
