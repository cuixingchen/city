package com.hdsx.taxi.woxing.cqcityserver.socket.utils;

/**
 * 数据转换工具
 * 
 * @author cuipengfei
 *
 */
public class HexStringUtil {

	public static String Bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			if (i != 0) {
				ret += ",";
			}
			ret += hex.toUpperCase();
		}
		ret = "[" + ret + "]";
		return ret;
	}

}
