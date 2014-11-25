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
 * <b>类名:Msg0x1014</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.16 0x1014 付款成功通知<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg1014 extends AbsMsg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg1014.class);

	private byte payment; // 0：现金 1：银行卡 2：微信 3：支付宝
	private String desc; // 文本内容

	@Override
	protected int getMsgID() {
		return MessageID.msg0x1014;
	}

	// @Override
	// protected int getBodylen() {
	// return 1+Converter.getBytes(desc).length+1;
	// }

	@Override
	public String toString() {
		return "Msg1014 [payment=" + payment + ", desc=" + desc + ", head="
				+ head + ", getMsgID()=" + getMsgID() + "]";
	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024); // 1 kb 缓冲区

		b.put(payment);

		ByteBuffer bc = ByteBuffer.allocate(Converter.getBytes(desc).length);
		bc.put(Converter.getBytes(desc));
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

			this.payment = f.get(offset);
			offset += 1;

			int stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			this.desc = Converter.toGBKString(f.array(), offset, stringEndIdx
					- offset);
			return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);
		}
		return false;

	}

	public byte getPayment() {
		return payment;
	}

	public void setPayment(byte payment) {
		this.payment = payment;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
