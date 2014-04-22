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
 * <b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.3 0x0003 通用应答<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg0003 extends AbsMsg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg0003.class);

	@Override
	public String toString() {
		return "Msg0003 [msgid=" + msgid + ", error=" + error + ", errorDesc="
				+ errorDesc + ", head=" + head + "]";
	}

	private short msgid; // 消息号 响应的消息ID，如响应2001抢单信息，则该ID为2001
	private byte error = 0; // 错误号 0-表示成功，1-表示消息格式错误，其它-暂为定义
	private String errorDesc = ""; // 错误消息

	@Override
	protected int getMsgID() {
		return MessageID.msg0x0003;
	}

	ByteBuffer buffer = ByteBuffer.allocate(1024); // 1kb 缓冲

	@Override
	protected byte[] bodytoBytes() {

		// 清空
		buffer.position(0);

		buffer.putShort(msgid);

		buffer.put(error);

		ByteBuffer b_validate = ByteBuffer.allocate(Converter
				.getBytes(errorDesc).length);
		buffer.put(b_validate.put(Converter.getBytes(errorDesc)).array());

		buffer.put((byte) 0x00);

		// 把当前 buffer 内容转换成 byte []
		byte[] result = new byte[buffer.position()];
		buffer.position(0);
		buffer.get(result);
		// 返回 新的 byte []
		return result;
	}

	protected boolean bodyfrombytes(byte[] b) {

		try {
			ByteBuffer bf = ByteBuffer.wrap(b);
			int offset = this.head.getBodylen();

			msgid = bf.getShort(offset);
			offset += 2;

			error = bf.get(offset);
			offset += 1;

			int stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			this.errorDesc = Converter.toGBKString(bf.array(), offset,
					stringEndIdx - offset);
			return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);
		}
		return false;

	}

	public short getMsgid() {
		return msgid;
	}

	public void setMsgid(short msgid) {
		this.msgid = msgid;
	}

	public byte getError() {
		return error;
	}

	public void setError(byte error) {
		this.error = error;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	// @Override
	// protected int getBodylen() {
	// return Converter
	// .getBytes(errorDesc).length+1+2+1;
	// }

}
