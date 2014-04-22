package com.hdsx.taxi.woxing.nettyutil.msg;


/**
 * 消息接口
 * @author Steven
 *
 */
public interface IMsg {
	byte[] toBytes();  
	boolean fromBytes(byte[] b);
}
