package com.hdsx.taxi.woxing.cqmsg.msg;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.MessageID;

/**
 * ***************************************************************************** <br/>
 * <b>类名:Msg0x0001</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：6.1.1 0x0001 客户端登录<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class Msg0001 extends AbsMsg {
	public static String VSS = "";

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg0001.class);

	private String timestamp; // 时间戳 BCD[7] YYYYMMDDHHnnss
	private String validate; // 验证码 MD5（YYYYMMDDHHnnss+VSS KEY）

	@Override
	protected int getMsgID() {
		return MessageID.msg0x0001;
	}

	@Override
	protected byte[] bodytoBytes() {
		try {
			ByteBuffer b = ByteBuffer.allocate(1024); // 1 kb 缓冲区

			Date d = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			this.timestamp = df.format(d);
			// 填充
			b.put(Converter.str2BCD(timestamp));
			MessageDigest md = MessageDigest.getInstance("MD5");

			this.validate = this.timestamp + VSS;
			md.update(this.validate.getBytes());

			b.put(md.digest());
			// ByteBuffer b_validate = ByteBuffer.allocate(16); // 指定长度
			// b.put(b_validate.put(Converter.getBytes(validate)).array());

			// 把当前 buffer 内容转换成 byte []
			byte[] result = new byte[b.position()];
			b.position(0);
			b.get(result);
			// 返回 新的 byte []
			return result;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {

		try {

			ByteBuffer bf = ByteBuffer.wrap(b);
			int offset = this.head.MSG_HEAD_LEN;

			timestamp = Converter.bcd2Str(b, offset, 7);
			offset += 7;

			this.validate = Converter.toGBKString(bf.array(), offset, 16);
			// offset += 16 ;

			return true;
		} catch (Exception ex) {

			logger.error("解析消息头失败", ex);
		}
		return false;

	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getValidate() {

		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	// protected int getBodylen() {
	// return 7 + 16;
	// }

}
