package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.FindEndFlag;
import com.hdsx.taxi.woxing.cqmsg.MessageID;

/**
 * *****************************************************************************
 * <br/><b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.11	0x1007 响应驾驶员取消申请<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class Msg1007 extends AbsMsg{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg1007.class);


	private short errorNumber ; // 错误号 0表示成功， 1表示失败
	private String errorDesc ; // 错误消息
	
	@Override
	protected int getMsgID() {
		return MessageID.msg0x1007;
	}	

//	@Override
//	protected int getBodylen() {
//		return 2+Converter.getBytes(errorDesc).length+1;
//	}

	@Override
	public String toString() {
		return "Msg1007 [errorNumber=" + errorNumber + ", errorDesc="
				+ errorDesc + ", head=" + head + ", getMsgID()=" + getMsgID()
				+ "]";
	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024);  // 1 kb 缓冲区
		
		b.putShort(errorNumber);
		
		ByteBuffer b_name = ByteBuffer.allocate(Converter.getBytes(errorDesc).length);
		b.put(b_name.put(Converter.getBytes(errorDesc)).array());
		
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
		
		ByteBuffer bf = ByteBuffer.wrap(b);
		int offset = this.head.getLength() ;
		
		errorNumber  = bf.getShort(offset);
		offset += 2 ;
		
		int stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
		this.errorDesc = Converter.toGBKString(bf.array(),offset,stringEndIdx - offset);
		return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);			
		}
		return false;
		
	}

	public short getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(short errorNumber) {
		this.errorNumber = errorNumber;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	
}
