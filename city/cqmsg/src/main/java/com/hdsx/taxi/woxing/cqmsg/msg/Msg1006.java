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
 * <b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.10 0x1006 乘客信息同步<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg1006 extends AbsMsg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg1006.class);

	private String name; // 乘客名称
	private String sex; // 乘客性别
	private String phone; // 乘客电话

	@Override
	protected int getMsgID() {
		return MessageID.msg0x1006;
	}

	// @Override
	// protected int getBodylen() {
	//
	// int le = Converter.getBytes(name).length + 1
	// +Converter.getBytes(sex).length + 1
	// +Converter.getBytes(phone).length + 1;
	//
	// return le;
	// }

	@Override
	public String toString() {
		return "Msg1006 [name=" + name + ", sex=" + sex + ", phone=" + phone
				+ ", head=" + head + ", getMsgID()=" + getMsgID() + "]";
	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(1024); // 1 kb 缓冲区

		ByteBuffer b_name = ByteBuffer
				.allocate(Converter.getBytes(name).length);
		b.put(b_name.put(Converter.getBytes(name)).array());

		b.put((byte) 0x00);

		ByteBuffer b_sex = ByteBuffer.allocate(Converter.getBytes(sex).length);
		b.put(b_sex.put(Converter.getBytes(sex)).array());

		b.put((byte) 0x00);

		ByteBuffer b_phone = ByteBuffer
				.allocate(Converter.getBytes(phone).length);
		b.put(b_phone.put(Converter.getBytes(phone)).array());

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
			int offset = this.head.getLength();

			int stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			name = Converter.toGBKString(b, offset, stringEndIdx - offset);

			offset = stringEndIdx;
			offset += 1;

			stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			sex = Converter.toGBKString(b, offset, stringEndIdx - offset);
			offset = stringEndIdx;
			offset += 1;

			stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			phone = Converter.toGBKString(bf.array(), offset, stringEndIdx
					- offset);
			offset = stringEndIdx;
			offset += 1;

			return true;
		} catch (Exception ex) {

			logger.error("解析消息体失败", ex);
		}
		return false;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
