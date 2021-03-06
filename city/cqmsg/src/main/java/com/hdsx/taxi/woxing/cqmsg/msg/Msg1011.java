package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.util.coor.CoordinateCodec;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.MessageID;

/**
 * *****************************************************************************
 * <br/><b>类名:Msg0x1011</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.13	0x1011 乘客上车通知<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class Msg1011 extends AbsMsg{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg1011.class);

	private double lng ; // 经度
	private double lat ; // 纬度
	private String bcdtime ; // 上车时间
	
	@Override
	protected int getMsgID() {
		return MessageID.msg0x1011;
	}	

//	@Override
//	protected int getBodylen() {
//		return 4+4+7;
//	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024);  // 1 kb 缓冲区
		b.put(Converter.unSigned32LongToBigBytes(CoordinateCodec.Coor2UInt(lng)));
		b.put(Converter.unSigned32LongToBigBytes(CoordinateCodec.Coor2UInt(lat)));
		b.put(Converter.str2BCD(bcdtime));
		// 把当前 buffer 内容转换成 byte []
		byte[] result = new byte[b.position()];
		b.position(0);
		b.get(result);
		// 返回 新的 byte [] 
		return result;
	}

	@Override
	public String toString() {
		return "Msg1011 [lng=" + lng + ", lat=" + lat + ", bcdtime=" + bcdtime
				+ ", head=" + head + ", getMsgID()=" + getMsgID() + "]";
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {
		
		try {
		
		ByteBuffer f = ByteBuffer.wrap(b);
		int offset = this.head.getLength();
		
		lng = CoordinateCodec.Coor2Float(Converter
				.bytes2Unsigned32Long(b, offset));
		offset += 4;

		lat = CoordinateCodec.Coor2Float(Converter
				.bytes2Unsigned32Long(b, offset));
		offset += 4 ;
		
		bcdtime = Converter.bcd2Str(f.array(), offset, 7);
		return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);			
		}
		return false;
		
	}

	
	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getBcdtime() {
		return bcdtime;
	}

	public void setBcdtime(String bcdtime) {
		this.bcdtime = bcdtime;
	}
	
}
