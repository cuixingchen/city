package com.hdsx.taxi.woxing.nettyutil.msg;


/**
 * 消息接口
 * @author Steven
 *
 */
public interface IMsg {
	byte[] tobytes();  
	int getLen();
	boolean frombytes(byte[] b);
}
