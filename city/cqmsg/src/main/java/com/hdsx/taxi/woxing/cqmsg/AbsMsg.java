package com.hdsx.taxi.woxing.cqmsg;

import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;

//TODO 实现抽象消息类
public class AbsMsg implements IMsg {

	@Override
	public byte[] tobytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLen() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean frombytes(byte[] b) {
		return false;
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param headbytes
	 *            消息头部分
	 * @param bodybytes
	 *            消息体部分
	 * @return
	 */
	public boolean frombytes(byte[] headbytes, byte[] bodybytes) {
		return false;
	}

}
