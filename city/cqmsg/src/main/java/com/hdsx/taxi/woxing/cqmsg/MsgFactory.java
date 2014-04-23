package com.hdsx.taxi.woxing.cqmsg;

import java.nio.ByteBuffer;

import com.hdsx.taxi.woxing.cqmsg.msg.Msg0001;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0002;

/**
 * 消息工厂
 * 
 * @author Steven
 * 
 */
public class MsgFactory {

	static ByteBuffer buffer = ByteBuffer.allocate(2);

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
		
		// TODO 完成消息工厂加载  @张阳
		//TODO 补充遗漏的消息 @谢光泉

		buffer.clear();
		buffer.put(bytes[0]);
		buffer.put(bytes[1]);

		short id = buffer.getShort();

		if (id == 0x0001)
			return new Msg0001();
		else if (id == 0x0002)
			return new Msg0002();

		return null;

	}

}
