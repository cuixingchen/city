package com.hdsx.taxi.woxing.cqmsg;

import java.util.ArrayList;
import java.util.List;

/**
 * ***************************************************************************** <br/>
 * <b>类名:FindEndFlag</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：....<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class FindEndFlag {

	/**
	 * 
	 * 方法名：findEndFlag <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年4月14日<br/>
	 * 功能描述：<br/>
	 * <b>从开始位找到指定位</b>
	 * 
	 * @param b
	 * @param offset
	 * @return
	 */
	public static int getFirstStringEndFlag(byte[] b, int offset) {
		for (int i = offset; i < b.length; i++) {
			if (b[i] == 0x00)
				return i;
		}
		return -1;
	}

	/**
	 * 
	 * 方法名：flagConverter <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年4月21日<br/>
	 * 功能描述：<br/>
	 * <b>添加转意处理</b>
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] flagPyramidConverter(byte[] b) {

		List<Byte> list = new ArrayList<Byte>();
		// 遍历所内容
		for (int i = 0; i < b.length; i++) {
			if (b[i] == 0x7e) {
				list.add((byte) 0x7d);
				list.add((byte) 0x02);
			} else if (b[i] == 0x7d) {
				list.add((byte) 0x7d);
				list.add((byte) 0x01);
			} else {
				list.add(b[i]);
			}
		}

		byte[] bf = new byte[list.size()];
		for (int i = 0; i < list.size(); i++) {
			bf[i] = list.get(i);
		}

		return bf;
	}

	public static byte[] flagDecodeConverter(byte[] b) {

		List<Byte> list = new ArrayList<Byte>();
		boolean flag = false;
		int index = 0;
		// 遍历所内容
		for (int i = 0; i < b.length; i++) {
			// 转标识位
			if (b[i] == 0x7d) {
				flag = true;
				index = i;
			}
			// 进行解吗
			if (flag) {
				if (i == (index + 1)) {
					if (b[i] == 0x02) {
						list.add((byte) 0x7e);
						flag = false;
					}
					if (b[i] == 0x01) {
						list.add((byte) 0x7d);
						flag = false;
					}
				}
			} else {
				list.add(b[i]);
			}

		}

		byte[] bf = new byte[list.size()];
		for (int i = 0; i < list.size(); i++) {
			bf[i] = list.get(i);
		}

		return bf;
	}


	public static void main(String[] args) {

//		byte[] b = { 0x30, 0x7e, 0x08, 0x7d, 0x55 };
//
//		byte[] bp = flagPyramidConverter(b);
//
//		for (byte c : bp) {
//			System.out.println(Integer.toHexString(c));
//		}

		System.out.println("-------------------------------");

//		byte[] bd = flagDecodeConverter(bp);
//
//		for (byte c : bd) {
//			System.out.println(Integer.toHexString(c));
//		}
		
		System.out.println("-------------------------------");
		
		
//		System.out.println(encode(bd));

	}

}
