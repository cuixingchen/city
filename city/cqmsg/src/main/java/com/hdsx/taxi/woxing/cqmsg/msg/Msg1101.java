package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.util.coor.CoordinateCodec;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.FindEndFlag;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.OrderInfo;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.PassengerInfo;

/**
 * ***************************************************************************** <br/>
 * <b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.5 0x1101 通知驾驶员抢单<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg1101 extends AbsMsg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg1101.class);

	private OrderInfo order;
	private short count;
	private List<String> carNumbers;

	@Override
	protected int getMsgID() {
		return MessageID.msg0x1101;
	}

	// @Override
	// protected int getBodylen() {
	//
	// int dl =
	// Converter.getBytes(order.getPassengerInfo().getPassengerName()).length+1
	// +1
	// +Converter.getBytes(order.getPassengerInfo().getPassengerPhone()).length+1
	// ;
	// int ol = 1+2+7
	// +Converter.getBytes(order.getAddress()).length+1
	// +4+4
	// +Converter.getBytes(order.getDestination()).length+1
	// +4+4
	// +Converter.getBytes(order.getRemark()).length+1;
	//
	// return dl+ol+2+carNumbers.size()*8;
	// }

	@Override
	public String toString() {
		return "Msg1101 [order=" + order + ", count=" + count + ", carNumbers="
				+ carNumbers + ", head=" + head + ", getMsgID()=" + getMsgID()
				+ "]";
	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024); // 1 kb 缓冲区

		// begin 司机信息
		byte[] opin = Converter.getBytes(order.getPassengerInfo()
				.getPassengerName());
		b.put(opin);
		b.put((byte) 0x00);

		b.put(order.getPassengerInfo().getPassengerSex());

		byte[] opip = Converter.getBytes(order.getPassengerInfo()
				.getPassengerPhone());
		b.put(opip);
		b.put((byte) 0x00);
		// end 司机信息

		// begin order
		b.put(order.getOrderType());
		b.putShort(order.getTips());
		b.put(Converter.str2BCD(order.getUseTime()));

		byte[] oua = Converter.getBytes(order.getAddress());
		b.put(oua);
		b.put((byte) 0x00);

		b.put(Converter.unSigned32LongToBigBytes(CoordinateCodec
				.Coor2UInt(order.getUseLng())));
		b.put(Converter.unSigned32LongToBigBytes(CoordinateCodec
				.Coor2UInt(order.getUseLat())));

		byte[] oudt = Converter.getBytes(order.getDestination());
		b.put(oudt);
		b.put((byte) 0x00);

		b.put(Converter.unSigned32LongToBigBytes(CoordinateCodec
				.Coor2UInt(order.getDestLng())));
		b.put(Converter.unSigned32LongToBigBytes(CoordinateCodec
				.Coor2UInt(order.getDestLat())));

		byte[] our = Converter.getBytes(order.getRemark());
		b.put(our);
		b.put((byte) 0x00);
		// end order
		b.putShort(count);

		for (String c : carNumbers) {
			b.put(Converter.getBytes(c));
		}

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

			order = new OrderInfo();
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
			order.setPassengerInfo(pi);
			// end 司机
			order.setOrderType(bf.get(offset));
			offset += 1;
			order.setTips(bf.getShort(offset));
			offset += 2;
			order.setUseTime(Converter.bcd2Str(b, offset, 7));
			offset += 7;
			stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			order.setAddress(Converter.toGBKString(b, offset, stringEndIdx
					- offset));
			offset = stringEndIdx;
			offset += 1;

			order.setUseLng(CoordinateCodec.Coor2Float(Converter.toUInt32(b,
					offset)));
			offset += 4;
			order.setUseLat(CoordinateCodec.Coor2Float(Converter.toUInt32(b,
					offset)));
			offset += 4;
			stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			order.setDestination(Converter.toGBKString(b, offset, stringEndIdx
					- offset));
			offset = stringEndIdx;
			offset += 1;
			order.setDestLng(CoordinateCodec.Coor2Float(Converter.toUInt32(b,
					offset)));
			offset += 4;
			order.setDestLat(CoordinateCodec.Coor2Float(Converter.toUInt32(b,
					offset)));
			offset += 4;
			stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			order.setRemark(Converter.toGBKString(b, offset, stringEndIdx
					- offset));
			offset = stringEndIdx;
			offset += 1;
			count = bf.getShort(offset);
			offset += 2;

			carNumbers = new ArrayList<String>();

			int end = b.length - offset;

			for (int i = 0; i < (end / 8); i++) {
				String temp = Converter.toGBKString(b, offset, 8);
				offset += 8;
				carNumbers.add(temp);
			}

			return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);
		}
		return false;

	}

	public OrderInfo getOrder() {
		return order;
	}

	public void setOrder(OrderInfo order) {
		this.order = order;
	}

	public short getCount() {
		return count;
	}

	public void setCount(short count) {
		this.count = count;
	}

	public List<String> getCarNumbers() {
		return carNumbers;
	}

	public void setCarNumbers(List<String> carNumbers) {
		this.carNumbers = carNumbers;
	}

}
