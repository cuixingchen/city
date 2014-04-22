package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.MessageID;

/**
 * *****************************************************************************
 * <br/><b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.2.5	0x2011 预约订单执行通知<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class Msg2011 extends AbsMsg{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg2011.class);


	private String carNumber ; // 车牌号
	private int lng ;  // 经度
	private int lat ; // 纬度
	private String bcdtime ; // 时间 yyyymmddhhnnss 
	
	@Override
	protected int getMsgID() {
		return MessageID.msg0x2011;
	}

//	@Override
//	protected int getBodylen() {
//		return 8+11+19+7+4+4;
//	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024);  // 1 kb 缓冲区
		
		ByteBuffer b_carNumber = ByteBuffer.allocate(8);
		b.put(b_carNumber.put(Converter.getBytes(carNumber)).array());
		
		b.put(Converter.unSigned32LongToBigBytes(lng));
		b.put(Converter.unSigned32LongToBigBytes(lat));
		
		b.put(Converter.str2BCD(bcdtime));
		
		
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
		int offset = this.head.getLength() ;

		this.carNumber = Converter.toGBKString(bf.array(),offset,8);
		offset += 8 ;
		
		lng = Converter.toUInt32(b, offset);
		offset += 4 ;
		
		lat = Converter.toUInt32(b, offset);
		offset += 4 ;

		bcdtime = Converter.bcd2Str(b, offset, 7);
		offset += 7 ;
		
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

	public String getBcdtime() {
		return bcdtime;
	}

	public void setBcdtime(String bcdtime) {
		this.bcdtime = bcdtime;
	}

	public int getLng() {
		return lng;
	}

	public void setLng(int lng) {
		this.lng = lng;
	}

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

}
