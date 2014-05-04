package com.hdsx.taxi.woxing.cqcityserver.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.MsgFactory;

public class CQTcpCodec extends ByteToMessageCodec<AbsMsg> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CQTcpCodec.class);

	private static final byte FLAG = 0x7e;
	ByteBuffer bf = ByteBuffer.allocate(4096);

	// /**
	// *
	// * @param ctx
	// * @param buffer
	// * @param out
	// * @throws Exception
	// */
	// protected void decode1(ChannelHandlerContext ctx, ByteBuf buffer,
	// List<Object> out) throws Exception {
	// if (!searchHead(buffer))
	// return;
	// int buffLen = buffer.readableBytes();
	// if (buffLen < MsgHeader.MSG_HEAD_LEN)
	// return;
	//
	// byte[] headbytes = new byte[MsgHeader.MSG_HEAD_LEN];
	//
	// ByteBuf msgbuffer = buffer.readBytes(headbytes);
	//
	// MsgHead head = new MsgHead();
	// head.frombytes(headbytes);
	// if (buffer.readableBytes() < head.getBodylen())
	// return;
	//
	// msgbuffer.writeBytes(buffer.readBytes(head.getBodylen()));
	//
	// AbsMsg msg = MsgFactory.genMsg(head.getId());
	//
	// if (msg.fromBytes(msgbuffer.array())) {
	// logger.debug("收到消息:" + msg.toString());
	// out.add(msg);
	// }
	// }

	@Override
	protected void encode(ChannelHandlerContext ctx, AbsMsg msg, ByteBuf out)
			throws Exception {

		byte[] bt = msg.toBytes();
		logger.debug("开始发送消息：" + msg.toString());
		if (logger.isDebugEnabled()) {

			StringBuilder sb = new StringBuilder();
			for (byte b : bt) {

				sb.append("[" + Integer.toHexString(b) + "]");
			}
			logger.debug("发送消息：" + sb.toString());

		}
		out.writeBytes(bt);
	}

	/**
	 * 查询消息头
	 * 
	 * @param buffer
	 * @return
	 */
	// private boolean searchHead(ByteBuf buffer) {
	//
	// while (buffer.readableBytes() > 0) {
	// if (buffer.readByte() == MsgHead.MSG_HEAD_FLAG)
	// return true;
	// }
	// return false;
	// }

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		logger.debug("收到消息,开始解码消息");
		while (in.readableBytes() > 0) {
			byte b = in.readByte();
			if (b != FLAG) {
				bf.put(b);
			} else {
				if (bf.position() > 0) {

					byte[] bytes = new byte[bf.position()];
					bf.position(0);
					bf.get(bytes);
					AbsMsg msg = MsgFactory.genMsg(bytes);
					msg.fromBytes(bytes);
					logger.debug("收到消息:" + msg.toString());
					if (msg != null) {
						out.add(msg);
					}
					bf.clear();
				}
			}
		}

	}

}
