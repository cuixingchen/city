package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.MessageID;

/**
 * 查询订单状态
 * @author cuipengfei
 *
 */
public class Msg1015 extends AbsMsg {

	@Override
	protected int getMsgID() {
		return MessageID.msg0x1015;
	}

	// @Override
	// protected int getBodylen() {
	// return 0;
	// }

	@Override
	protected byte[] bodytoBytes() {
		return new byte[0];

	}

	@Override
	public String toString() {
		return "Msg1015 [head=" + head + ", getMsgID()=" + getMsgID() + "]";
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {

		return true;

	}

}
