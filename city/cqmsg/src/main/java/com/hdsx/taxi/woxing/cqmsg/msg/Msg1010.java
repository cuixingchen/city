package com.hdsx.taxi.woxing.cqmsg.msg;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.MessageID;

/**
 * *****************************************************************************
 * <br/><b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.12	0x1010 车辆位置查询<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class Msg1010 extends AbsMsg{


	
	@Override
	protected int getMsgID() {
		return MessageID.msg0x1010;
	}	

//	@Override
//	protected int getBodylen() {
//		return 0;
//	}

	@Override
	protected byte[] bodytoBytes() {
		return null;
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {
		return true ;
	}


	
}
