package com.hdsx.taxi.woxing.cqmsg;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 
 * *****************************************************************************
 * <br/><b>类名:Converter</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年3月7日<br/>
 * 功能：字节流转化类<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class Converter {


	public static String reversal(String str) {
		// System.out.println(str);
		String tep = "";
		for (int i = str.length() - 1; i >= 0; i--) {
			tep = tep + str.charAt(i);
		}

		return tep;
	}

	/**
	 * 把无符号16位整数转换成byte数组
	 * 
	 * @param val
	 *            : int 存放16位无符号整数的
	 * @param b
	 * @param pos
	 *            起始位置
	 */
	public static void unSigned16Int2Bytes(int val, byte[] b, int pos) {
		// b[pos] = (byte) (val);
		// b[pos + 1] = (byte) (val >> 8);
		b[pos + 1] = (byte) (val);
		b[pos] = (byte) (val >> 8);
	}

	/**
	 * 把字节数组转换成16位无符号整数
	 * 
	 * @param b
	 * @param pos
	 *            起始位置
	 * @return
	 */
	public static int bytes2UnSigned16Int(byte[] b, int pos) {
		// return (b[pos] & 0xff) + (b[pos + 1] << 8 & 0xff00);
		return (b[pos + 1] & 0xff) + (b[pos] << 8 & 0xff00);
	}

	public static byte[] unSigned16Int2Bytes(int val) {
		byte[] b = new byte[2];
		// b[0] = (byte) (val);
		// b[1] = (byte) (val >> 8);
		b[1] = (byte) (val);
		b[0] = (byte) (val >> 8);
		return b;
	}

	/**
	 * 把字节数组转换成32位无符号整数
	 * 
	 * @param b
	 *            byte数组 小端
	 * @param pos
	 *            位置
	 * @return
	 */
	public static long bytes2Unsigned32Long(byte[] b, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		int index = pos;
		// firstByte = (0x000000FF & ((int) b[index]));
		// secondByte = (0x000000FF & ((int) b[index + 1]));
		// thirdByte = (0x000000FF & ((int) b[index + 2]));
		// fourthByte = (0x000000FF & ((int) b[index + 3]));
		firstByte = (0x000000FF & ((int) b[index + 3]));
		secondByte = (0x000000FF & ((int) b[index + 2]));
		thirdByte = (0x000000FF & ((int) b[index + 1]));
		fourthByte = (0x000000FF & ((int) b[index]));
		index = index + 4;
		return ((long) (fourthByte << 24 | thirdByte << 16 | secondByte << 8 | firstByte)) & 0xFFFFFFFFL;
	}

	public static byte[] int2bytes(int n) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			// b[i] = (byte) (n >> i * 8);
			b[3 - i] = (byte) (n >> i * 8);
		}
		return b;

	}

	public static byte[] long2bytes(long n) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			// b[i] = (byte) (n >> i * 8);
			b[3 - i] = (byte) (n >> i * 8);
		}
		return b;

	}

	public static long bigBytes2Unsigned32Long(byte[] b, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		int index = pos;
		firstByte = (0x000000FF & ((int) b[index + 3]));
		secondByte = (0x000000FF & ((int) b[index + 2]));
		thirdByte = (0x000000FF & ((int) b[index + 1]));
		fourthByte = (0x000000FF & ((int) b[index + 0]));
		index = index + 4;
		return ((long) (fourthByte << 24 | thirdByte << 16 | secondByte << 8 | firstByte)) & 0xFFFFFFFFL;
	}

	/**
	 * LHY
	 * 
	 * @param str
	 *            必须为2的倍数，且为纯字符串
	 * @return
	 */
	public static byte[] str2BCD(String str) {
		if (str == null)
			throw new IllegalArgumentException("输入转换参数为null!");
		int strLen = str.length();
		if (strLen % 2 != 0) {
			throw new IllegalArgumentException("参数不合法,长度必须为2的倍数!");
		}
		int len = strLen / 2;
		byte[] bcd = new byte[len];
		byte[] temp = str.getBytes();
		for (int i = temp.length; i > 0; i -= 2) {
			byte low = (byte) (temp[i - 1] - 48);
			byte high = (byte) (temp[i - 2] - 48);
			bcd[(i / 2 - 1)] = (byte) (((high << 4) & 0xff) + low);
		}
		return bcd;

	}

	/**
	 * 把无符号32位无符号整数转换成字节数组
	 * 
	 * @param val
	 *            32位无符号整数
	 * @param b
	 * @param pos
	 *            位置
	 */
	public static void unSigned32LongToBytes(long val, byte[] b, int pos) {
		// b[pos] = (byte) (val >> 0);
		// b[pos + 1] = (byte) (val >> 8);
		// b[pos + 2] = (byte) (val >> 16);
		// b[pos + 3] = (byte) (val >> 24);
		b[pos + 3] = (byte) (val >> 0);
		b[pos + 2] = (byte) (val >> 8);
		b[pos + 1] = (byte) (val >> 16);
		b[pos] = (byte) (val >> 24);
	}

	public static byte[] unSigned32LongToBytes(long val) {
		byte[] b = new byte[4];
		// b[0] = (byte) (val >> 0);
		// b[1] = (byte) (val >> 8);
		// b[2] = (byte) (val >> 16);
		// b[3] = (byte) (val >> 24);
		b[3] = (byte) (val >> 0);
		b[2] = (byte) (val >> 8);
		b[1] = (byte) (val >> 16);
		b[0] = (byte) (val >> 24);
		return b;
	}

	public static void unSigned32LongToBigBytes(long val, byte[] b, int pos) {
		b[pos + 3] = (byte) (val >> 0);
		b[pos + 2] = (byte) (val >> 8);
		b[pos + 1] = (byte) (val >> 16);
		b[pos + 0] = (byte) (val >> 24);
	}

	public static byte[] unSigned32LongToBigBytes(long val) {
		byte[] b = new byte[4];
		b[3] = (byte) (val >> 0);
		b[2] = (byte) (val >> 8);
		b[1] = (byte) (val >> 16);
		b[0] = (byte) (val >> 24);
		return b;
	}

	/**
	 * 把byte数组里从第pos位置开始的len个长度转化成String
	 * 
	 * @param b
	 * @param pos
	 * @param len
	 */
	public static String bytes2String(byte[] b, int pos, int len) {
		byte[] str = new byte[len];
		System.arraycopy(b, pos, str, 0, len);

		return new String(str, Charset.forName("gbk"));
	}

	public static String bytes2utf8String(byte[] b, int pos, int len) {

		int l = 0;
		for (int i = 0; i < len; i++) {
			if (b[pos + i] == 0)
				break;
			l++;
		}
		byte[] str = new byte[l];
		System.arraycopy(b, pos, str, 0, l);

		return new String(str,Charset.forName("utf-8"));
	}

	public static void StringFill2Bytes(byte[] b, String str, int pos) {
		byte[] s = str.getBytes();
		int len = s.length;
		for (int i = 0; i < len; i++) {
			b[pos + i] = s[i];
		}
	}

	public static void StringBCDFill2Bytes(byte[] b, String str, int pos) {
		int len = str.getBytes(Charset.forName("gbk")).length / 2;
		byte[] temp = new byte[len];
		temp = str2BCD(str);
		System.arraycopy(temp, 0, b, pos, len);
	}

	public static void StringASCFill2Bytes(byte[] b, String str, int pos) {
		int len = str.getBytes(Charset.forName("US-ASCII")).length;
		byte[] temp = new byte[len];
		temp = string2ASCII(str);
		System.arraycopy(temp, 0, b, pos, len);
	}

	/**
	 * 把字节数组转换成char
	 * 
	 * @param b
	 * @param pos
	 *            起始位置
	 * @return
	 */
	public static char bytes2Char(byte[] b, int pos) {
		return (char) (b[pos] + (b[pos + 1] << 8));
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bcd2Str(byte[] b, int from, int len) {
		byte[] bytes = new byte[len];
		System.arraycopy(b, from, bytes, 0, len);

		return bcd2Str(bytes);
	}

	public static String bcd2Str(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(byte2HexStr(bytes[i]));
		}
		return sb.toString();
	}

	/**
	 * 将char转换为小端字节数组
	 * 
	 * @param val
	 * @param b
	 * @param from
	 * @param len
	 * @return len
	 */
	public static byte[] char2bytes(char c) {
		byte[] b = new byte[2];
		b[0] = (byte) (c);
		b[1] = (byte) (c >> 8);
		return b;
	}

	/**
	 * short转为字节数组
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] getBytes(short n) {
		byte[] b = new byte[2];
		// b[0] = (byte) (0xFF & n);
		// b[1] = (byte) ((0xFF00 & n) >> 8);
		b[1] = (byte) (0xFF & n);
		b[0] = (byte) ((0xFF00 & n) >> 8);
		return b;
	}

	/**
	 * int 转为字节数组
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] getBytes(int n) {
		byte[] b = new byte[4];
		// b[0] = (byte) (0xFF & n);
		// b[1] = (byte) ((0xFF00 & n) >> 8);
		// b[2] = (byte) ((0xFF0000 & n) >> 16);
		// b[3] = (byte) ((0xFF000000 & n) >> 24);
		b[3] = (byte) (0xFF & n);
		b[2] = (byte) ((0xFF00 & n) >> 8);
		b[1] = (byte) ((0xFF0000 & n) >> 16);
		b[0] = (byte) ((0xFF000000 & n) >> 24);
		return b;
	}

	/**
	 * long转Bytes
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] getBytes(long n) {

		int n1 = (int) (0xFFFF & n);
		byte[] b1 = getBytes(n1);
		int n2 = (int) ((n - n1) >> 32);
		byte[] b2 = getBytes(n2);
		byte[] r = new byte[b1.length + b2.length];
		// System.arraycopy(b1, 0, r, 0, 4);
		// System.arraycopy(b2, 0, r, 4, 4);
		System.arraycopy(b2, 0, r, 0, 4);
		System.arraycopy(b1, 0, r, 4, 4);
		return r;
	}

	/**
	 * String转GBK Bytes
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] getBytes(String s) {
		return s==null?"".getBytes(Charset.forName("GBK")):s.getBytes(Charset.forName("GBK"));
	}

	/**
	 * byte[] 转String
	 * 
	 * @param b
	 * @return
	 */
	public static String toGBKString(byte[] b) {
		try {
			String s = new String(b, "GBK");
			return s;
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * byte[] 转String
	 * 
	 * @param b
	 * @param startIndex
	 * @param length
	 * @return
	 */
	public static String toGBKString(byte[] b, int startIndex, int length) {
		byte[] b1 = new byte[length];
		System.arraycopy(b, startIndex, b1, 0, length);
		return toGBKString(b1);
	}

	/**
	 * String 转为定长的BCD码
	 * 
	 * @param s
	 * @param BCDLength
	 * @return
	 */
	public static byte[] string2BCD(String s, int BCDLength) {
		if (s.length() / 2 != BCDLength) {
			throw new IllegalArgumentException("希望长度为:" + BCDLength + ",实际为："
					+ s.length() / 2);
		}
		return str2BCD(s);

	}

	/**
	 * BCD转Float
	 * 
	 * @param b
	 *            BCD
	 * @param dotlen
	 *            小数点位数
	 * @return
	 */
	public static float bcd2Float(byte[] b, int dotlen) {

		long n = Converter.bcd2int(b);
		double fac = Math.pow(10, dotlen);
		float r = (float) (n / fac);
		return r;
		//
		// String s = bcd2string(b);
		// StringBuilder sb = new StringBuilder(s);
		// sb.insert(s.length() - dotlen * 2, ".");

		// return Float.parseFloat(sb.toString());
	}

	public static float bcd2Float(byte[] b, int startIndex, int length,
			int dotlen) {
		byte[] s = new byte[length];
		System.arraycopy(b, startIndex, s, 0, length);
		return bcd2Float(s, dotlen);
	}

	/**
	 * Float转BCD
	 * 
	 * @param f
	 *            Float
	 * @param dotlen
	 *            小数点位数
	 * @return
	 */
	public static byte[] float2bcd(float f, int dotlen) {

		double fac = Math.pow(10, dotlen);
		int n = (int) (f * (int) fac);
		return Converter.int2bcd(n);

	}

	public static byte[] float2bcd(float f, int dotlen, int BCDLength) {
		byte[] b = float2bcd(f, dotlen);
		if (b.length == BCDLength)
			return b;
		byte[] r = new byte[BCDLength];
		for (int i = 0; i < r.length; i++) {
			r[i] = 0;
		}
		System.arraycopy(b, 0, r, BCDLength - b.length, b.length);
		return r;
	}

	/**
	 * BCD转int
	 * 
	 * @param b
	 * @return
	 */
	public static int bcd2int(byte[] b) {
		int n = 0;

		for (int i = 0; i < b.length; i++) {
			n = n * 100 + bcd2Value(b[i]);
		}
		return n;
	}

	public static int bcd2int(byte[] b, int startIndex, int length) {
		byte[] s = new byte[length];
		System.arraycopy(b, startIndex, s, 0, length);
		return bcd2int(s);
	}

	/**
	 * 整数类型转换为BCD编码
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] int2bcd(int n) {
		// Integer n1 = new Integer(n);
		// String s = n1.toString();
		// return Converter.string2BCD(s);

		ArrayList<Byte> list = new ArrayList<Byte>();

		while (n > 0) {
			int v = n % 100;
			byte b = _value2BCD(v);
			list.add(new Byte(b));
			n = n / 100;
		}
		byte[] r = ArrayUtils.toPrimitive(list.toArray(new Byte[0]));
		ArrayUtils.reverse(r);
		return r;

	}

	/**
	 * byte转换为BCD值 如89(十进制)转换为01001001
	 * 
	 * @param b
	 * @return
	 */
	private static byte _value2BCD(int b) {

		int m = b % 10;// 低位
		int n = b / 10;// 高位
		return (byte) (((n & 0x0F) << 4) + (m & 0x0F));
	}

	/**
	 * BCD值转换为数值,如153（10011001）转换为99(10进制)；
	 * 
	 * @param b
	 * @return
	 */
	public static int bcd2Value(byte b) {
		int low = b & 0x0F;
		int high = (b & 0xF0) >> 4;
		return high * 10 + low;

	}

	public static byte[] int2bcd(int n, int BCDLength) {
		byte[] b = Converter.int2bcd(n);
		if (b.length == BCDLength)
			return b;
		byte[] r = new byte[BCDLength];
		for (int i = 0; i < r.length; i++) {
			r[i] = 0;
		}
		System.arraycopy(b, 0, r, BCDLength - b.length, b.length);
		return r;

	}

	public static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	public static short bytes2Unsigned8Short(byte b) {
		Byte bb = new Byte(b);
		if (bb.shortValue() < 0)
			return (short) (bb.shortValue() + 256);

		return bb.shortValue();
	}

	/**
	 * yy-mm-dd-hh-mm-ss格式字符串转换成6位BCD码
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] date2Bcd(String str) {
		String[] arr = str.split("-");
		byte[] bytes = new byte[6];
		for (int i = 0; i < 6; i++) {
			bytes[i] = (byte) ((((byte) (Integer.parseInt(arr[i]
					.substring(0, 1)))) << 4) + ((byte) (Integer
					.parseInt(arr[i].substring(1)))));
		}

		return bytes;
	}

	/**
	 * 6位BCD码转换成 yy-mm-dd-hh-mm-ss格式字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String bcd2Date(byte[] b) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 6; i++) {
			sb.append((b[i] & 0xf0) >> 4).append((b[i] & 0x0f)).append("-");

		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	public static String byte2HexStr(byte b) {
		String hs = "";
		String stmp = "";
		stmp = (Integer.toHexString(b & 0XFF));
		if (stmp.length() == 1)
			hs = hs + "0" + stmp;
		else
			hs = hs + stmp;
		// if (n<b.length-1) hs=hs+":";
		return hs.toUpperCase();
	}

	public static int toUInt32(byte[] b, int flag) {
		// TODO Auto-generated method stub
		return (int) Converter.bytes2Unsigned32Long(b, flag);
	}

	public static short toUInt16(byte[] b, int flag) {
		// byte b1 = b[flag];
		// byte b2 = b[flag + 1];
		byte b2 = b[flag];
		byte b1 = b[flag + 1];
		int r = (b1 >> 8) + b2;
		return (short) r;
	}

	public static String BCD2Date(byte[] b, int start, int length) {
		byte[] a = new byte[length];
		System.arraycopy(b, start, a, 0, length);
		return bcd2Date(a);
	}

	/**
	 * @date 2010/06/11
	 * 
	 * @author xijie
	 * 
	 * @param Input
	 *            String-contents
	 * @return Integer-array
	 */
	public static byte[] string2ASCII(String s) {// 字符串转换为ASCII码

		byte[] bytes = null;
		try {
			bytes = s.getBytes("US-ASCII");
		} catch (Exception e) {
		}
		return bytes;
	}

	public static int char2ASCII(char c) {
		return (int) c;
	}

	/**
	 * @date 2010/06/11
	 * 
	 * @author xijie
	 * @param ASCIIs
	 *            ASCII编码字符串
	 * @return
	 */
	public static String ascii2String(int size, byte[] aSCIIs, int index) {
		String aString = null;

		byte[] asciiByte = new byte[size];
		for (int i = 0; i < asciiByte.length; i++) {
			asciiByte[i] = aSCIIs[index];
			index++;
		}

		try {
			aString = new String(asciiByte, "US-ASCII");
		} catch (Exception e) {
		}
		return aString.trim();
	}

	public static char ascii2Char(int ASCII) {
		return (char) ASCII;
	}

	/**
	 * @author wusq 获得消息中String类型字段的长度
	 * @param flag
	 * @param b
	 * @return
	 */
	public static int getLength(int flag, byte[] b) {
		int len = -1;
		for (int i = flag; i < b.length; i++) {
			len++;
			if (b[i] == 0) {
				return len;
			}
		}
		return len;
	}

	/*
	 * public static int bytes2int(byte[] b) { int firstByte = 0; int secondByte
	 * = 0; int thirdByte = 0; int fourthByte = 0; firstByte = (0x000000FF &
	 * ((int) b[0])); secondByte = (0x000000FF & ((int) b[1])); thirdByte =
	 * (0x000000FF & ((int) b[2])); fourthByte = (0x000000FF & ((int) b[3]));
	 * return ((int) (fourthByte << 24 | thirdByte << 16 | secondByte << 8 |
	 * firstByte)) & 0xFFFFFFFF;
	 * 
	 * }
	 */

	public static void long2Bytes(long x, byte[] bb, int index) {
		// bb[index + 7] = (byte) (x >> 56);
		// bb[index + 6] = (byte) (x >> 48);
		// bb[index + 5] = (byte) (x >> 40);
		// bb[index + 4] = (byte) (x >> 32);
		// bb[index + 3] = (byte) (x >> 24);
		// bb[index + 2] = (byte) (x >> 16);
		// bb[index + 1] = (byte) (x >> 8);
		// bb[index + 0] = (byte) (x >> 0);
		bb[index + 0] = (byte) (x >> 56);
		bb[index + 1] = (byte) (x >> 48);
		bb[index + 2] = (byte) (x >> 40);
		bb[index + 3] = (byte) (x >> 32);
		bb[index + 4] = (byte) (x >> 24);
		bb[index + 5] = (byte) (x >> 16);
		bb[index + 6] = (byte) (x >> 8);
		bb[index + 7] = (byte) (x >> 0);
	}

	public static long bytes2Long(byte[] bb, int index) {
		// return ((((long) bb[index + 7] & 0xff) << 56)
		// | (((long) bb[index + 6] & 0xff) << 48)
		// | (((long) bb[index + 5] & 0xff) << 40)
		// | (((long) bb[index + 4] & 0xff) << 32)
		// | (((long) bb[index + 3] & 0xff) << 24)
		// | (((long) bb[index + 2] & 0xff) << 16)
		// | (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] &
		// 0xff) << 0));
		return ((((long) bb[index + 0] & 0xff) << 56)
				| (((long) bb[index + 1] & 0xff) << 48)
				| (((long) bb[index + 2] & 0xff) << 40)
				| (((long) bb[index + 3] & 0xff) << 32)
				| (((long) bb[index + 4] & 0xff) << 24)
				| (((long) bb[index + 5] & 0xff) << 16)
				| (((long) bb[index + 6] & 0xff) << 8) | (((long) bb[index + 7] & 0xff) << 0));
	}

	// =============
	/**
	 * 处理所有小数点BCD码 营运上传指令里交易前余额 dot 小数点位置 比如56.24 为2
	 */
	public static String bcd2DotString(byte[] bytes, int startIndex, int len,
			int dot) {

		byte[] b = new byte[len];
		System.arraycopy(bytes, startIndex, b, 0, len);
		String temp = bcd2Str(b);
		temp = temp.substring(0, temp.length() - dot) + "."
				+ temp.substring(temp.length() - dot);
		while (temp.startsWith("0")) {
			int start = temp.indexOf("0");
			temp = temp.substring(start + 1);
		}

		return temp;
	}

	public static byte[] bcddotStr2Bytes(String str) {
		str = str.replace(".", "");
		return str2BCD(str);
	}

	/**
	 * 把字节数组抓换成十六进制字符串
	 * 
	 * @param b
	 * @param flag
	 * @param i
	 *            长度
	 * @return
	 */
	public static String byte2HexStr(byte[] b, int flag, int i) {
		byte[] temp = new byte[i];
		System.arraycopy(b, flag, temp, 0, i);
		return byte2HexStr(temp);

	}

	/**
	 * 高位在前
	 * 
	 * @param hex
	 * @param bytes
	 * @param index
	 */
	public static void hexStr2Bytes(String hex, byte[] bytes, int index) {
		int n = hex.length() / 2;
		for (int i = 0; i < n; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
					16);
		}
	}

	public static void main(String[] args) {
//		System.out.println(Integer.toHexString((int) ("很".toCharArray()[0])));
		
		int f = 129;
		
		byte tf = (byte) f;
		
		System.out.println("byte:"+tf);
		
		int rf = (tf & 0xff);
		
		System.out.println("int:"+rf);
		
	}
	
	
	

}
