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
 * 功能：6.2.3	0x2005 驾驶员取消订单通知<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class Msg2005 extends AbsMsg{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg2005.class);



	private String carNumber ; // 车牌号
	private String phone ; // 手机号
	private String certificate ; // 驾驶员的从业资格证号
	private String bcdtime ; // 时间 yyyymmddhhnnss 
	private byte cause ;  //  取消原因 0-遇堵，1-事故，2-其它，3-乘客爽约
	
	@Override
	protected int getMsgID() {
		return MessageID.msg0x2005;
	}

//	@Override
//	protected int getBodylen() {
//		return 8+11+19+7+1;
//	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024);  // 1 kb 缓冲区
		
		ByteBuffer b_carNumber = ByteBuffer.allocate(8);
		b.put(b_carNumber.put(Converter.getBytes(carNumber)).array());
		
		ByteBuffer b_phone = ByteBuffer.allocate(11);
		b.put(b_phone.put(Converter.getBytes(phone)).array());
		
		ByteBuffer b_certificate = ByteBuffer.allocate(19);
		b.put(b_certificate.put(Converter.getBytes(certificate)).array());
		
		b.put(Converter.str2BCD(bcdtime));
		
		b.put(cause);
		
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
		
		this.phone = Converter.toGBKString(bf.array(),offset,11);
		offset += 11 ;
		
		this.certificate = Converter.toGBKString(bf.array(),offset,19);
		offset += 19 ;
		
		bcdtime = Converter.bcd2Str(b, offset, 7);
		offset += 7 ;
		
		cause = bf.get(offset);
		
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getBcdtime() {
		return bcdtime;
	}

	public void setBcdtime(String bcdtime) {
		this.bcdtime = bcdtime;
	}

	public byte getCause() {
		return cause;
	}

	public void setCause(byte cause) {
		this.cause = cause;
	}


}
