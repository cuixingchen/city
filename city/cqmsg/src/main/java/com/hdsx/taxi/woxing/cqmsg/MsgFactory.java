package com.hdsx.taxi.woxing.cqmsg;

import java.nio.ByteBuffer;

import com.hdsx.taxi.woxing.cqmsg.msg.Msg0001;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0002;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0003;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1001;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1002;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1003;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1004;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1005;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1006;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1007;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1010;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1011;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1012;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1013;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1014;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1015;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1016;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1101;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2001;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2005;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2010;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2011;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2012;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2013;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2015;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg3003;

/**
 * 消息工厂
 * 
 * @author Steven
 * 
 */
public class MsgFactory {


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

		// TODO 完成消息工厂加载 @张阳
		// TODO 补充遗漏的消息 @谢光泉

		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.clear();
		buffer.put(bytes[2]);
		buffer.put(bytes[3]);

		buffer.position(0);
//		buffer.position(2);
		short id = buffer.getShort();
		 

		int msgID = id;
		AbsMsg m = null;
		switch (msgID) {
		case MessageID.msg0x0001:
			m = new Msg0001();
			break;
		case MessageID.msg0x0002:
			m = new Msg0002();
			break;
		case MessageID.msg0x0003:
			m = new Msg0003();
			break;
		case MessageID.msg0x1001:
			m = new Msg1001();
			break;
		case MessageID.msg0x1002:
			m = new Msg1002();
			break;
		case MessageID.msg0x1003:
			m = new Msg1003();
			break;
		case MessageID.msg0x1004:
			m = new Msg1004();
			break;
		case MessageID.msg0x1005:
			m = new Msg1005();
			break;
		case MessageID.msg0x1006:
			m = new Msg1006();
			break;
		case MessageID.msg0x1007:
			m = new Msg1007();
			break;
		case MessageID.msg0x1010:
			m = new Msg1010();
			break;
		case MessageID.msg0x1011:
			m = new Msg1011();
			break;
		case MessageID.msg0x1012:
			m = new Msg1012();
			break;
		case MessageID.msg0x1013:
			m = new Msg1013();
			break;
		case MessageID.msg0x1014:
			m = new Msg1014();
			break;
		case MessageID.msg0x1015:
			m = new Msg1015();
			break;
		case MessageID.msg0x1016:
			m = new Msg1016();
			break;
		case MessageID.msg0x1101:
			m = new Msg1101();
			break;
		case MessageID.msg0x2001:
			m = new Msg2001();
			break;
		case MessageID.msg0x2005:
			m = new Msg2005();
			break;
		case MessageID.msg0x2010:
			m = new Msg2010();
			break;
		case MessageID.msg0x2011:
			m = new Msg2011();
			break;
		case MessageID.msg0x2012:
			m = new Msg2012();
			break;
		case MessageID.msg0x2013:
			m = new Msg2013();
			break;
		case MessageID.msg0x2015:
			m = new Msg2015();
			break;
		case MessageID.msg0x3003:
			m = new Msg3003();
			break;

		default:
			break;
		}
		// if (id == 0x0001)
		// return new Msg0001();
		// else if (id == 0x0002)
		// return new Msg0002();

		return m;

	}

}
