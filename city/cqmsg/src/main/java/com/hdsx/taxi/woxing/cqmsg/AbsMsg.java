package com.hdsx.taxi.woxing.cqmsg;

import java.nio.ByteBuffer;

import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;

/**
 * 
 * ***************************************************************************** <br/>
 * <b>类名:AbsMsg</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月11日<br/>
 * 功能：消息组成<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public abstract class AbsMsg implements IMsg {

	static int curSn = Integer.MIN_VALUE; // 自动增加的消息流水号
	public static byte THIRD_PART_FLAG = 0; // 第三方接入平台标识
	/**
	 * 
	 */
	ByteBuffer buffer = ByteBuffer.allocate(1024);

	protected MsgHeader head;

	public AbsMsg() {
		this.head = new MsgHeader();
		this.head.setMsgid((short) getMsgID());
		this.head.setSn(curSn++);
		this.head.setFlag(THIRD_PART_FLAG);
	}

	/**
	 * 
	 * 方法名：toBytes <br/>
	 * 编写人：谢广泉<br/>
	 * 日期: 2014年4月11日<br/>
	 * 功能描述：<br/>
	 * <b>通过实体对象转化成 字节流</b>
	 * 
	 * @return
	 */
	public byte[] toBytes() {

		byte[] body = bodytoBytes();
		this.head.setBodylen((short) body.length);
		byte[] head = this.head.tobytes();

		byte xor = 0;

		for (byte b : head) {

			xor ^= b;
		}
		for (byte b : body) {
			xor ^= b;
		}

		body = encode(body);
		head = encode(head);
		byte[] xorbytes = encode(new byte[] { xor });

		buffer.position(0);
		buffer.put(MsgHeader.MSG_HEAD_FLAG);
		buffer.put(head);
		buffer.put(body);
		buffer.put(xorbytes);
		buffer.put(MsgHeader.MSG_HEAD_FLAG);
		byte[] result = new byte[buffer.position()];
		buffer.position(0);

		buffer.get(result);
		return result;

	}

	private byte[] encode(byte[] bytes) {

		buffer.position(0);
		for (byte b : bytes) {
			if (b == 0x7e) {
				buffer.put((byte) 0x7d);
				buffer.put((byte) 0x02);
			} else if (b == 0x7d) {
				buffer.put((byte) 0x7d);
				buffer.put((byte) 0x01);
			} else {
				buffer.put(b);
			}
		}

		byte[] result = new byte[buffer.position()];
		buffer.position(0);
		buffer.get(result);
		return result;
	}

	/**
	 * 
	 * 方法名：fromBytes <br/>
	 * 编写人：谢广泉<br/>
	 * 日期: 2014年4月11日<br/>
	 * 功能描述：<br/>
	 * <b>转化成实体对象</b>
	 * 
	 * @param b
	 */
	public boolean fromBytes(byte[] b) {
		b = decode(b);
		byte xor = 0;
		for (int i = 0; i < b.length - 1; i++) {
			xor ^= b[i];
		}
		if (xor != b[b.length - 1])
			return false;

		this.head = new MsgHeader();
		if (!this.head.frombytes(b))
			return false;
		if (!bodyfrombytes(b))
			return false;
		return true;
		// 标识位

	}

	private byte[] decode(byte[] b) {
		ByteBuffer buffer1 = ByteBuffer.wrap(b);
		buffer.position(0);
		while (buffer1.remaining() > 0) {

			byte d = buffer1.get();
			if (d == 0x7d) {
				byte e = buffer1.get();
				if (e == 0x02)
					buffer.put((byte) 0x7e);
				else if (e == 0x01)
					buffer.put((byte) 0x7d);
			} else {
				buffer.put(d);
			}
		}

		byte[] result = new byte[buffer.position()];
		buffer.position(0);
		buffer.get(result);
		return result;
	}

	/**
	 * 
	 * 方法名：getMsgID <br/>
	 * 编写人：谢广泉<br/>
	 * 日期: 2014年4月11日<br/>
	 * 功能描述：<br/>
	 * <b>标示</b>
	 * 
	 * @return
	 */
	protected abstract int getMsgID();

	/**
	 * 
	 * 方法名：bodytoBytes <br/>
	 * 编写人：谢广泉<br/>
	 * 日期: 2014年4月11日<br/>
	 * 功能描述：<br/>
	 * <b>消息体转化成 bytes</b>
	 * 
	 * @return
	 */
	protected abstract byte[] bodytoBytes();

	/**
	 * 
	 * 方法名：bodyfrombytes <br/>
	 * 编写人：谢广泉<br/>
	 * 日期: 2014年4月11日<br/>
	 * 功能描述：<br/>
	 * <b>bytes 转化成 object</b>
	 * 
	 * @param b
	 */
	protected abstract boolean bodyfrombytes(byte[] b);

	/**
	 * 
	 * 方法名：getHeader <br/>
	 * 编写人：谢广泉<br/>
	 * 日期: 2014年4月11日<br/>
	 * 功能描述：<br/>
	 * <b>获取消息头方法</b>
	 * 
	 * @return
	 */
	public MsgHeader getHeader() {
		return head;
	}

}
