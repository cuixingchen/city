package com.hdsx.taxi.woxing.cqmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * ***************************************************************************** <br/>
 * <b>类名:MsgHeader</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月3日<br/>
 * 功能：消息头<br/>
 * 消息头协议是采用 “重庆市城市出租汽车服务管理信息系统试点工程-电召应用改进方案V0.8_20140409张阳增加消息定义.doc” 消息格式
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class MsgHeader implements Serializable {
	@Override
	public String toString() {
		return "MsgHeader [msgid=" + msgid + ", sn=" + sn + ", flag=" + flag
				+ ", orderid=" + orderid + ", bodylen=" + bodylen + "]";
	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MsgHeader.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private short msgid; // 消息ID
	private int sn; // 消息流水号
	private byte flag; // 第三方接入平台标识
	private long orderid = 0; // 订单号
	private short bodylen; // 剩余消息总长度

	public final static byte MSG_HEAD_FLAG = 0x7e; // 标识位
	public final static byte MSG_HEAD_LEN = 2 + 4 + 1 + 4 + 2;

	// 0x7e
	/**
	 * 
	 * 方法名：tobytes <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年4月11日<br/>
	 * 功能描述：<br/>
	 * <b>xxxx</b>
	 * 
	 * @return
	 */
	public byte[] tobytes() {
		ByteBuffer b = ByteBuffer.allocate(getLength());

		b.putShort((short) msgid);
		b.putInt(sn);
		b.put((byte) flag);
		b.put(Converter.unSigned32LongToBigBytes(orderid));
		b.putShort((short) bodylen);

		return b.array();

	}

	public boolean frombytes(byte[] b) {
		try {
			ByteBuffer bf = ByteBuffer.wrap(b);
			int offset = 0;

			this.msgid = bf.getShort(offset);
			offset += 2;

			this.sn = bf.getInt(offset);
			offset += 4;

			this.flag = bf.get(offset);
			offset += 1;

			this.orderid = Converter.bigBytes2Unsigned32Long(b, offset);
			offset += 4;

			this.bodylen = bf.getShort(offset);
			return true;
		} catch (Exception ex) {

			logger.error("解析消息头失败", ex);
		}
		return false;

	}

	/**
	 * 
	 * 方法名：getLength <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年4月11日<br/>
	 * 功能描述：<br/>
	 * <b>头部消息长度</b>
	 * 
	 * @return
	 */
	public int getLength() {
		return 2 + 4 + 1 + 4 + 2;
	}

	public short getMsgid() {
		return msgid;
	}

	public void setMsgid(short msgid) {
		this.msgid = msgid;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public long getOrderid() {
		return orderid;
	}

	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}

	public short getBodylen() {
		return bodylen;
	}

	public void setBodylen(short bodylen) {
		this.bodylen = bodylen;
	}



}
