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
 * 功能：6.1.3	0x0003 通用应答<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class Msg3003 extends AbsMsg{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg3003.class);


	private short msgid ; // 消息号   响应的消息ID，如响应2001抢单信息，则该ID为2001
	private byte error ; // 错误号   0-表示成功，1-表示消息格式错误，其它-暂为定义
	private String errorDesc ; // 错误消息
	
	@Override
	protected int getMsgID() {
		return MessageID.msg0x3003;
	}

//	@Override
//	protected int getBodylen() {
//		return 2+1+Converter.getBytes(errorDesc).length+1;
//	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024);  // 1 kb 缓冲区
		
		b.putShort(msgid);
		
		b.put(error) ;
		
		b.put(Converter.getBytes(errorDesc));
		
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
		
		
		msgid = bf.getShort(offset);
		offset  += 2 ;
		
		error  = bf.get(offset);
		offset += 1 ;
		
		
		int stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
		this.errorDesc = Converter.toGBKString(bf.array(),offset,stringEndIdx - offset );
		return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);			
		}
		return false;
		
	}

	public short getMsgid() {
		return msgid;
	}


	@Override
	public String toString() {
		return "Msg3003 [msgid=" + msgid + ", error=" + error + ", errorDesc="
				+ errorDesc + ", head=" + head + ", getMsgID()=" + getMsgID()
				+ "]";
	}

	public void setMsgid(short msgid) {
		this.msgid = msgid;
	}

	public byte getError() {
		return error;
	}

	public void setError(byte error) {
		this.error = error;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

}
