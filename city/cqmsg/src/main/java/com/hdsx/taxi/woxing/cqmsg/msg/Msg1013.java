package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.FindEndFlag;
import com.hdsx.taxi.woxing.cqmsg.MessageID;

/**
 * ***************************************************************************** <br/>
 * <b>类名:Msg0x1013</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.15 0x1013 文本信息通知<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg1013 extends AbsMsg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg1013.class);

	private String context; // 文本内容

	@Override
	protected int getMsgID() {
		return MessageID.msg0x1013;
	}

	//
	// @Override
	// protected int getBodylen() {
	// return Converter.getBytes(context).length+1;
	// }

	@Override
	public String toString() {
		return "Msg1013 [context=" + context + ", head=" + head
				+ ", getMsgID()=" + getMsgID() + "]";
	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024); // 1 kb 缓冲区

		ByteBuffer bc = ByteBuffer.allocate(Converter.getBytes(context).length);
		bc.put(Converter.getBytes(context));
		b.put(bc.array());

		b.put((byte) 0x00);

		// 把当前 buffer 内容转换成 byte []
		byte[] result = new byte[b.position()];
		b.position(0);
		b.get(result);
		// 返回 新的 byte []
		return result;
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {

		try {

			ByteBuffer f = ByteBuffer.wrap(b);
			int offset = this.head.getLength();

			int stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			this.context = Converter.toGBKString(f.array(), offset,
					stringEndIdx - offset);

			return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);
		}
		return false;

	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

}
