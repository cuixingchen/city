package com.hdsx.taxi.woxing.cqmsg;

import java.nio.ByteBuffer;

import com.hdsx.taxi.woxing.nettyutil.msg.IMsgHead;

public class MsgHead implements IMsgHead {

	public final static byte MSG_HEAD_FLAG = 0x7e; // 标识位

	public final static byte MSG_HEAD_LEN = 1 + 2 + 4 + 1 + 4 + 2;
	short id;
	int sn;
	byte thirdpart_flag;
	long orderid;
	short bodylen;

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public byte getThirdpart_flag() {
		return thirdpart_flag;
	}

	public void setThirdpart_flag(byte thirdpart_flag) {
		this.thirdpart_flag = thirdpart_flag;
	}

	public long getOrderid() {
		return orderid;
	}

	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}

	public short getBodylen() {
		return bodylen;
	}

	public void setBodylen(short bodylen) {
		this.bodylen = bodylen;
	}

	@Override
	public byte[] tobytes() {
		ByteBuffer b = ByteBuffer.allocate(MSG_HEAD_LEN);
		b.put(MSG_HEAD_FLAG);
		b.putShort(this.id);

		b.putInt(this.sn);
		b.put(this.thirdpart_flag);

		return null;
	}

	@Override
	public int getHeadLen() {

		return 0;
	}

	@Override
	public void frombytes(byte[] b) {
		// TODO Auto-generated method stub

	}

}
