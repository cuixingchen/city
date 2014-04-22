package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.MessageID;

/**
 * *****************************************************************************
 * <br/><b>类名:Msg0x1014</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.18	0x1016 查询指定位置附近的车辆<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class Msg1016 extends AbsMsg{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg1016.class);

	
	private byte state ; // 车辆状态
	private int lng ; // 经度
	private int lat ; // 纬度
	private short radius; // 半径
	
	@Override
	protected int getMsgID() {
		return MessageID.msg0x1016;
	}	

//	@Override
//	protected int getBodylen() {
//		return 1+4+4+2;
//	}

	@Override
	protected byte[] bodytoBytes() {
		
		ByteBuffer b = ByteBuffer.allocate(1024);  // 1 kb 缓冲区
		
		b.put(state);
		b.put(Converter.unSigned32LongToBigBytes(lng));
		b.put(Converter.unSigned32LongToBigBytes(lat));
		b.put(Converter.unSigned16Int2Bytes(radius));
		
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
		
		this.state = f.get(offset);
		offset += 1 ;
		
		this.lng = Converter.toUInt32(b, offset);
		offset += 4 ;
		
		this.lat = Converter.toUInt32(b, offset);
		offset += 4 ;
		
		this.radius = (short) Converter.bytes2UnSigned16Int(b, offset);
		
		return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);			
		}
		return false;
		
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
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

	public short getRadius() {
		return radius;
	}

	public void setRadius(short radius) {
		this.radius = radius;
	}
	
}
