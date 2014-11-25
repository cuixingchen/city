package com.hdsx.taxi.woxing.cqcityserver.socket.thread;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.socket.HandlerFactory;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
import com.hdsx.taxi.woxing.cqmsg.MsgFactory;
import com.hdsx.taxi.woxing.cqmsg.MsgHeader;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

/**
 * 处理消息线程
 * 
 * @author cuipengfei
 * 
 */
public class ParseMsgThread extends Thread {

	private static final Logger logger = LoggerFactory
			.getLogger(ParseMsgThread.class);

	private byte[] rpb;

	public ParseMsgThread(byte[] rpb) {
		this.rpb = rpb;
	}

	@Override
	public void run() {
		try {
			// 转码
			this.rpb = decode(this.rpb);

			// 消息头解析
			MsgHeader head = headFromBytes(rpb);
			if (head == null) {
				return;
			}

			// 生成消息
			AbsMsg msg = MsgFactory.genMsg(head);
			msg.fromBytes(rpb);
			logger.debug("接收到数据解码后："+msg.toString());
			int msgid = msg.getHeader().getMsgid();
			if (msgid != MessageID.msg0x2010) {
				if(logger.isInfoEnabled()){
					logger.info("接收到数据解码后："+msg.toString());
				}
				TcpClient.getInstance().sendAnsworMsg(msg);
			}
			IHandler handler = HandlerFactory.getHandler(msg);
			if (handler != null) {
				handler.doHandle(msg);
			}
		} catch (Exception e) {
			logger.error("接受消息队列处理数据错误", e);
		}
	}

	/**
	 * 消息头解析
	 * 
	 * @return
	 */
	private MsgHeader headFromBytes(byte[] b) {
		byte xor = 0;
		for (int i = 0; i < b.length - 1; i++) {
			xor ^= b[i];
		}
		if (xor != b[b.length - 1])
			return null;

		MsgHeader head = new MsgHeader();
		if (!head.frombytes(b))
			return null;
		return head;
	}

	/**
	 * 解码转义
	 * 
	 * @param b
	 * @return
	 */
	private byte[] decode(byte[] b) {
		ByteBuffer buffer1 = ByteBuffer.wrap(b);
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		buffer.position(0);
		while (buffer1.remaining() > 0) {

			byte d = buffer1.get();
			if (d == 0x7d) {
				byte e = buffer1.get();
				if (e == 0x02)
					buffer.put((byte) 0x7e);
				else if (e == 0x01)
					buffer.put((byte) 0x7d);
			} else {
				buffer.put(d);
			}
		}

		byte[] result = new byte[buffer.position()];
		buffer.position(0);
		buffer.get(result);
		return result;
	}

}
