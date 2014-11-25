package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.MessageID;

/**
 * ***************************************************************************** <br/>
 * <b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.7 0x1003 抢单成功通知<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg1003 extends AbsMsg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Msg1003.class);

	private String carNumber; // 车牌号

	@Override
	protected int getMsgID() {
		return MessageID.msg0x1003;
	}

	// @Override
	// protected int getBodylen() {
	// return 8;
	// }

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024); // 1 kb 缓冲区

		ByteBuffer b_validate = ByteBuffer.allocate(8);
		b.put(b_validate.put(Converter.getBytes(carNumber)).array());

		// 把当前 buffer 内容转换成 byte []
		byte[] result = new byte[b.position()];
		b.position(0);
		b.get(result);
		// 返回 新的 byte [] 
		return result;
	}

	@Override
	public String toString() {
		return "Msg1003 [carNumber=" + carNumber + ", head=" + head
				+ ", getMsgID()=" + getMsgID() + "]";
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {

		try {
			ByteBuffer bf = ByteBuffer.wrap(b);
			int offset = this.head.getLength();

			this.carNumber = Converter.toGBKString(bf.array(), offset, 8);
			return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);
		}
		return false;

	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

}
