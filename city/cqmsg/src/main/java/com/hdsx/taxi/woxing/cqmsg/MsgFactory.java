package com.hdsx.taxi.woxing.cqmsg;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * 消息工厂
 * 
 * @author Steven
 * 
 */
public class MsgFactory {

	static ByteBuffer buffer = ByteBuffer.allocate(2);
	static HashMap<Short, AbsMsg> map = new HashMap<>();

	static {
		// TODO 初始化消息load

	}

	/**
	 * 通过消息id生成消息
	 * 
	 * @param msgid
	 * @return
	 */
	public static AbsMsg genMsg(int msgid) {
		return null;

	}

	/**
	 * 根据二进制文件生成消息
	 * 
	 * @param bytes
	 * @return
	 */
	public static AbsMsg genMsg(byte[] bytes) {

		buffer.clear();
		buffer.put(bytes[0]);
		buffer.put(bytes[1]);

		short id = buffer.getShort();

		AbsMsg msg = map.get(id);
		if (msg.fromBytes(bytes))
			return msg;
		return null;

	}

}
