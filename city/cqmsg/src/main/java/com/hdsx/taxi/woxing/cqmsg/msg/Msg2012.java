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
 * 功能：6.2.6	0x2012 付款通知<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class Msg2012 extends AbsMsg{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg2012.class);



	private String carNumber ; // 车牌号
	private String certificate ; // 驾驶员的从业资格证号
	private short sum ;  // 交易金额
	private short cost ; // 电召费用
	private String bcdtime ; // 时间 yyyymmddhhnnss 
	
	@Override
	protected int getMsgID() {
		return MessageID.msg0x2012;
	}

//	@Override
//	protected int getBodylen() {
//		return 8+19+2+2+7;
//	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024);  // 1 kb 缓冲区
		
		ByteBuffer b_carNumber = ByteBuffer.allocate(8);
		b.put(b_carNumber.put(Converter.getBytes(carNumber)).array());
		
		ByteBuffer b_certificate = ByteBuffer.allocate(19);
		b.put(b_certificate.put(Converter.getBytes(certificate)).array());
		
		b.putShort(sum);
		b.putShort(cost);
		
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
		
		this.certificate = Converter.toGBKString(bf.array(),offset,19);
		offset += 19 ;
		
		this.sum = bf.getShort(offset);
		offset += 2 ;
		this.cost = bf.getShort(offset);
		offset += 2 ;

		this.bcdtime = Converter.bcd2Str(b, offset, 7);
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

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public short getSum() {
		return sum;
	}

	public void setSum(short sum) {
		this.sum = sum;
	}

	public short getCost() {
		return cost;
	}

	public void setCost(short cost) {
		this.cost = cost;
	}

	public String getBcdtime() {
		return bcdtime;
	}

	public void setBcdtime(String bcdtime) {
		this.bcdtime = bcdtime;
	}

	@Override
	public String toString() {
		return "Msg2012 [carNumber=" + carNumber + ", certificate="
				+ certificate + ", sum=" + sum + ", cost=" + cost
				+ ", bcdtime=" + bcdtime + ", head=" + head + ", getMsgID()="
				+ getMsgID() + "]";
	}


}
