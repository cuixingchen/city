package com.hdsx.taxi.woxing.cqcityserver.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.socket.utils.HexStringUtil;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.MsgFactory;

public class CQTcpCodec extends ByteToMessageCodec<AbsMsg> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CQTcpCodec.class);

	private static final byte FLAG = 0x7e;
	private static final int Head_Length = 13;
	ByteBuffer bf = ByteBuffer.allocate(4096);

	@Override
	protected void encode(ChannelHandlerContext ctx, AbsMsg msg, ByteBuf out)
			throws Exception {

		byte[] bt = msg.toBytes();
		out.writeBytes(bt);
		logger.debug("发送原始数据："+HexStringUtil.Bytes2HexString(bt));
	}

	/**
	 * 查询消息头
	 * 
	 * @param buffer
	 * @return
	 */
//	private boolean searchHead(ByteBuf buffer) {
//
//		int i = 0;
//		int buffLen = buffer.readableBytes();
//		int readIndex = buffer.readerIndex();
//		int startFlat = 0;
//		while (i < buffLen) {
//			startFlat = buffer.getByte(readIndex + i);
//			if (startFlat==FLAG) {
//				if (i > 0)
//					buffer.skipBytes(i);
//				return true;
//			}
//			i++;
//		}
//		return false;
//	}

//	@Override
//	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
//			List<Object> out) throws Exception {
//		try {
//			if (!searchHead(buffer))
//				return;
//			int buffLen = buffer.readableBytes();
//			int index = buffer.readerIndex();
//			if (buffLen < Head_Length)
//				return;
//
//			byte[] lenBytes = new byte[2];
//			buffer.getBytes(index + 1, lenBytes);
//			int len = Converter.bytes2UnSigned16Int(lenBytes, 0);// 获取剩余消息长度
//			// int dataLen = len +
//			// TCPConstants.TCP_HEADER_LENGTH;//加上消息头长度后数据总长度
//			if (len == 0)
//				return;
//			int dataLen = 1+13+len+1+1;// 后修改的整个包的大小应该包含消息头的长度;
//			if (buffLen < dataLen)
//				return;
//
//			byte[] msgbytes = new byte[dataLen-2];
//			buffer.skipBytes(1);
//			buffer.readBytes(msgbytes);
//			buffer.skipBytes(1);
//			AbsMsg m = MsgFactory.genMsg(msgbytes);
//			m.fromBytes(msgbytes);
//			out.add(m);
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//
//	}
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		while (in.readableBytes() > 0) {
			byte b = in.readByte();
			if (b != FLAG) {
				bf.put(b);
			} else {
				if (bf.position() > 0) {

					byte[] bytes = new byte[bf.position()];
					bf.position(0);
					bf.get(bytes);
					if (bytes.length>0) {
						logger.debug("接受到原始数据：" + HexStringUtil.Bytes2HexString(bytes));
						out.add(bytes);
					}
					bf.clear();
				}
			}
		}

	}

	static String getBytesHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (Byte b : bytes) {
			sb.append("[" + Integer.toHexString(b) + "]");
		}
		return sb.toString();
	}
}
