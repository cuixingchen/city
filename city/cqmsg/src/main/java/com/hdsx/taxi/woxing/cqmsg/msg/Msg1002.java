package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.FindEndFlag;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.PassengerInfo;

/**
 * ***************************************************************************** <br/>
 * <b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.6 0x1002 订单撤销<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg1002 extends AbsMsg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg1002.class);


	private PassengerInfo passenger; // 乘客信息
	private byte cause; // 撤单原因 0表示驾驶员爽约，1表示等待时间太长，2表示乘客有车了
	private String carNubmer; // 车牌号

	@Override
	public String toString() {
		return "Msg1002 [passenger=" + passenger + ", cause=" + cause
				+ ", carNubmer=" + carNubmer + ", head=" + head
				+ ", getMsgID()=" + getMsgID() + "]";
	}

	@Override
	protected int getMsgID() {
		return MessageID.msg0x1002;
	}

	@Override
	protected byte[] bodytoBytes() {

		ByteBuffer b = ByteBuffer.allocate(1024);

		// begin 司机信息
		b.put(Converter.getBytes(passenger.getPassengerName()));

		b.put((byte) 0x00);

		b.put(passenger.getPassengerSex());

		b.put(Converter.getBytes(passenger.getPassengerPhone()));

		b.put((byte) 0x00);
		// end 司机信息
		b.put(cause);

		ByteBuffer bc = ByteBuffer.allocate(8);
		bc.put(Converter.getBytes(carNubmer));
		b.put(bc.array());

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

			// being 司机
			PassengerInfo pi = new PassengerInfo();
			int stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			pi.setPassengerName(Converter.toGBKString(b, offset, stringEndIdx
					- offset));
			offset = stringEndIdx;
			offset += 1;
			pi.setPassengerSex(bf.get(offset));
			offset += 1;
			stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			pi.setPassengerPhone(Converter.toGBKString(b, offset, stringEndIdx
					- offset));
			offset = stringEndIdx;
			offset += 1;
			// end 司机
			this.passenger = pi;

			cause = bf.get(offset);
			offset += 1;

			carNubmer = Converter.toGBKString(b, offset, 8);
			return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);
		}
		return false;

	}

	public PassengerInfo getPassenger() {
		return passenger;
	}

	public void setPassenger(PassengerInfo passenger) {
		this.passenger = passenger;
	}

	public byte getCause() {
		return cause;
	}

	public void setCause(byte cause) {
		this.cause = cause;
	}

	public String getCarNubmer() {
		return carNubmer;
	}

	public void setCarNubmer(String carNubmer) {
		this.carNubmer = carNubmer;
	}

}
