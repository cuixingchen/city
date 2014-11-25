package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.CarInfo;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.MessageID;

/**
 * ***************************************************************************** <br/>
 * <b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：0x2016 查询周边空车结果<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg2016 extends AbsMsg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Msg2016.class);

	private short count; // 空车数量
	private List<CarInfo> ls ; // 车辆信息组

	@Override
	protected int getMsgID() {
		return MessageID.msg0x2016;
	}

	// @Override
	// protected int getBodylen() {
	// return 1;
	// }

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024);  // 1 kb 缓冲区

		b.putShort(count);
		
		for (CarInfo ci : ls) {
			long lon = (long)(ci.getLon()*10E6);
			long lat = (long)(ci.getLat()*10E6);
			b.put(Converter.unSigned32LongToBigBytes(lon));
			b.put(Converter.unSigned32LongToBigBytes(lat));
			
			ByteBuffer b_name = ByteBuffer.allocate(16);
			b_name.put(Converter.getBytes(ci.getDriverName()));
			b.put(b_name.array());
			
			ByteBuffer b_serial = ByteBuffer.allocate(19);
			b_serial.put(Converter.getBytes(ci.getDriverSerial()));
			b.put(b_serial.array());
			
			ByteBuffer b_number = ByteBuffer.allocate(8);
			b_number.put(Converter.getBytes(ci.getLisencenumber()));
			b.put(b_number.array());
			
		}

		// 把当前 buffer 内容转换成 byte []
		byte[] result = new byte[b.position()];
		b.position(0);
		b.get(result);
		// 返回 新的 byte [] 
		return result;
	}

	@Override
	public String toString() {
		return "Msg2016 [count=" + count + ", ls=" + ls + ", head=" + head
				+ ", getMsgID()=" + getMsgID() + "]";
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {

		try {

			ByteBuffer bf = ByteBuffer.wrap(b);
			int offset = this.head.getLength();

			count = bf.getShort(offset);
			offset += 2 ; 
			
			ls = new ArrayList<CarInfo>();
			
			for (int i = 0; i < count; i++) {
				
				CarInfo ci = new CarInfo();
				
				ci.setLon(Converter.toUInt32(b, offset));
				offset += 4 ;
				
				ci.setLat(Converter.toUInt32(b, offset));
				offset += 4 ;
				
				ci.setDriverName(Converter.toGBKString(b, offset, 16));
				offset += 16;
				
				ci.setDriverSerial(Converter.toGBKString(b, offset, 19));
				offset += 19;
				
				ci.setLisencenumber(Converter.toGBKString(b, offset, 8));
				offset += 8;
				
				ls.add(ci);
			}

			return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);
		}
		return false;

	}

	public short getCount() {
		return count;
	}

	public void setCount(short count) {
		this.count = count;
	}

	public List<CarInfo> getLs() {
		return ls;
	}

	public void setLs(List<CarInfo> ls) {
		this.ls = ls;
	}


}
