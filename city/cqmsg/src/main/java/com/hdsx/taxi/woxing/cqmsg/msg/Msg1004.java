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
 * 功能：6.1.8 0x1004 抢单失败通知<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg1004 extends AbsMsg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg1004.class);

	private String carNumber; // 车牌号
	private byte error; // 错误号 0-表示成功，其它-暂为定义
	private String errorDesc; // 错误消息

	@Override
	protected int getMsgID() {
		return MessageID.msg0x1004;
	}

	//
	// @Override
	// protected int getBodylen() {
	// return 8+1+Converter.getBytes(errorDesc).length+1;
	// }

	@Override
	public String toString() {
		return "Msg1004 [carNumber=" + carNumber + ", error=" + error
				+ ", errorDesc=" + errorDesc + ", head=" + head
				+ ", getMsgID()=" + getMsgID() + "]";
	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024); // 1 kb 缓冲区

		ByteBuffer b_carNumber = ByteBuffer.allocate(8);
		b.put(b_carNumber.put(Converter.getBytes(carNumber)).array());

		b.put(error);

		ByteBuffer b_validate = ByteBuffer.allocate(Converter
				.getBytes(errorDesc).length);
		b.put(b_validate.put(Converter.getBytes(errorDesc)).array());

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

			ByteBuffer bf = ByteBuffer.wrap(b);
			int offset = this.head.getLength();

			this.carNumber = Converter.toGBKString(bf.array(), offset, 8);
			offset += 8;

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

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
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

}
