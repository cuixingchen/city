package com.hdsx.taxi.woxing.cqcityserver.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.MsgFactory;
import com.hdsx.taxi.woxing.cqmsg.MsgHead;

public class CQTcpCodec extends ByteToMessageCodec<AbsMsg> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CQTcpCodec.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
			List<Object> out) throws Exception {
		if (!searchHead(buffer))
			return;
		int buffLen = buffer.readableBytes();
		if (buffLen < MsgHead.MSG_HEAD_LEN)
			return;

		byte[] headbytes = new byte[MsgHead.MSG_HEAD_LEN];

		ByteBuf msgbuffer = buffer.readBytes(headbytes);

		MsgHead head = new MsgHead();
		head.frombytes(headbytes);
		if (buffer.readableBytes() < head.getBodylen())
			return;

		msgbuffer.writeBytes(buffer.readBytes(head.getBodylen()));

		AbsMsg msg = MsgFactory.genMsg(head.getId());

		if (msg.frombytes(msgbuffer.array())) {
			logger.debug("收到消息:" + msg.toString());
			out.add(msg);
		}
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, AbsMsg msg, ByteBuf out)
			throws Exception {

		logger.debug("开始发送消息：" + msg.toString());
		byte[] bt = msg.tobytes();
		out.writeBytes(bt);
	}

	/**
	 * 查询消息头
	 * 
	 * @param buffer
	 * @return
	 */
	private boolean searchHead(ByteBuf buffer) {

		while (buffer.readableBytes() > 0) {
			if (buffer.readByte() == MsgHead.MSG_HEAD_FLAG)
				return true;
		}
		return false;
	}

}
