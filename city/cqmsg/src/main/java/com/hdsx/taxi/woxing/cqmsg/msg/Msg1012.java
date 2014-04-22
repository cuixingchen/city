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
 * <b>类名:Msg0x1011</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.14 0x1012 乘客位置更新通知<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg1012 extends AbsMsg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg1012.class);

	private String bcdtime; // 上车时间
	private int lng; // 经度
	private int lat; // 纬度
	private String desc; // 描述信息

	@Override
	protected int getMsgID() {
		return MessageID.msg0x1012;
	}

	// @Override
	// protected int getBodylen() {
	// return 4+4+7+Converter.getBytes(desc).length+1;
	// }

	@Override
	protected byte[] bodytoBytes() {

		ByteBuffer b = ByteBuffer.allocate(1024); // 1 kb 缓冲区
		b.put(Converter.str2BCD(bcdtime));
		b.put(Converter.unSigned32LongToBigBytes(lng));
		b.put(Converter.unSigned32LongToBigBytes(lat));

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

			bcdtime = Converter.bcd2Str(f.array(), offset, 7);
			offset += 7;

			lng = Converter.toUInt32(b, offset);
			offset += 4;

			lat = Converter.toUInt32(b, offset);
			offset += 4;

			int stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			this.desc = Converter.toGBKString(f.array(), offset, stringEndIdx
					- offset);
			return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);
		}
		return false;

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

	public String getBcdtime() {
		return bcdtime;
	}

	public void setBcdtime(String bcdtime) {
		this.bcdtime = bcdtime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
