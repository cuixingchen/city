package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.OrderInfo;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.PassengerInfo;

/**
 * ***************************************************************************** <br/>
 * <b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.4 0x1001 发送订单<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg0x1001 extends AbsMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OrderInfo order;
	private int radius;

	@Override
	protected int getMsgID() {
		return MessageID.msg0x1001;
	}

	@Override
	protected int getBodylen() {
		int dl = Converter
				.getBytes(order.getPassengerInfo().getPassengerName()).length
				+ 1
				+ 1
				+ Converter.getBytes(order.getPassengerInfo()
						.getPassengerPhone()).length + 1;
		int ol = 1 + 2 + 7 + Converter.getBytes(order.getAddress()).length + 1
				+ 4 + 4 + Converter.getBytes(order.getDestination()).length + 1
				+ 4 + 4 + Converter.getBytes(order.getRemark()).length + 1;
		return dl + ol + 4;
	}

	@Override
	protected byte[] bodytoBytes() {

		ByteBuffer b = ByteBuffer.allocate(1024);

		// begin 司机信息
		b.put(Converter.getBytes(order.getPassengerInfo().getPassengerName()));

		b.put((byte) 0x00);

		b.put(order.getPassengerInfo().getPassengerSex());

		b.put(Converter.getBytes(order.getPassengerInfo().getPassengerPhone()));

		b.put((byte) 0x00);
		// end 司机信息

		// begin order
		b.put(order.getOrderType());
		b.putShort(order.getTips());
		// b.put(Converter.date2Bcd(order.getUseTime()));

		b.put(Converter.str2BCD(order.getUseTime()));

		byte[] oua = Converter.getBytes(order.getAddress());
		b.put(oua);
		b.put((byte) 0x00);

		b.put(Converter.unSigned32LongToBigBytes(order.getUseLng()));
		b.put(Converter.unSigned32LongToBigBytes(order.getUseLat()));

		byte[] oudt = Converter.getBytes(order.getDestination());
		b.put(oudt);
		b.put((byte) 0x00);

		b.put(Converter.unSigned32LongToBigBytes(order.getDestLng()));
		b.put(Converter.unSigned32LongToBigBytes(order.getDestLat()));

		byte[] our = Converter.getBytes(order.getRemark());
		b.put(our);
		b.put((byte) 0x00);
		b.putInt(radius);
		// end order

		byte[] result = new byte[b.position()];
		b.position(0);

		b.get(result);

		return result;
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {
		return false;

//		ByteBuffer bf = ByteBuffer.wrap(b);
//		int offset = this.head.getBodylen();
//
//		order = new OrderInfo();
//		// being 司机
//		PassengerInfo pi = new PassengerInfo();
//		int stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
//		pi.setPassengerName(Converter.toGBKString(b, offset, stringEndIdx
//				- offset));
//		offset = stringEndIdx;
//		offset += 1;
//		pi.setPassengerSex(bf.get(offset));
//		offset += 1;
//		stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
//		pi.setPassengerPhone(Converter.toGBKString(b, offset, stringEndIdx
//				- offset));
//		offset = stringEndIdx;
//		offset += 1;
//		order.setPassengerInfo(pi);
//		// end 司机
//		order.setOrderType(bf.get(offset));
//		offset += 1;
//		order.setTips(bf.getShort(offset));
//		offset += 2;
//		order.setUseTime(Converter.bcd2Str(b, offset, 7));
//		offset += 7;
//		stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
//		order.setAddress(Converter
//				.toGBKString(b, offset, stringEndIdx - offset));
//		offset = stringEndIdx;
//		offset += 1;
//		order.setUseLng(Converter.toUInt32(b, offset));
//		offset += 4;
//		order.setUseLat(Converter.toUInt32(b, offset));
//		offset += 4;
//		stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
//		order.setDestination(Converter.toGBKString(b, offset, stringEndIdx
//				- offset));
//		offset = stringEndIdx;
//		offset += 1;
//		order.setDestLng(Converter.toUInt32(b, offset));
//		offset += 4;
//		order.setDestLat(Converter.toUInt32(b, offset));
//		offset += 4;
//		stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
//		order.setRemark(Converter.toGBKString(b, offset, stringEndIdx - offset));
//		offset = stringEndIdx;
//		offset += 1;
//		radius = bf.getInt(offset);

	}

	public OrderInfo getOrder() {
		return order;
	}

	public void setOrder(OrderInfo order) {
		this.order = order;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

}
