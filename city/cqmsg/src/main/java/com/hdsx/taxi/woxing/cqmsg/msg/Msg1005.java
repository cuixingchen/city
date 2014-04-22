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
 * 功能：6.1.8	0x1004 抢单失败通知<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class Msg1005 extends AbsMsg{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg1005.class);


	private byte type ; // 评价类型
	private String context ; // 错误消息
	
	@Override
	protected int getMsgID() {
		return MessageID.msg0x1005;
	}

//	@Override
//	protected int getBodylen() {
//		return 1+Converter.getBytes(context).length+1;
//	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024);  // 1 kb 缓冲区
		
		b.put(type) ;
		
		ByteBuffer b_validate = ByteBuffer.allocate(Converter.getBytes(context).length);
		b.put(b_validate.put(Converter.getBytes(context)).array());
		
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
		
		type  = bf.get(offset);
		offset += 1 ;
		
		int stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
		this.context = Converter.toGBKString(bf.array(),offset,stringEndIdx - offset);
		return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);			
		}
		return false;
		
	}
	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
}
