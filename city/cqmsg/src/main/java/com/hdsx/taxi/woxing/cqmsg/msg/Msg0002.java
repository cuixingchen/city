package com.hdsx.taxi.woxing.cqmsg.msg;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.MessageID;

/**
 * ***************************************************************************** <br/>
 * <b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.2 0x0002 心跳同步<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg0002 extends AbsMsg {

	@Override
	protected int getMsgID() {
		return MessageID.msg0x0002;
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
		return "Msg0002 [head=" + head + ", getMsgID()=" + getMsgID() + "]";
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {
		return true;

	}

}
