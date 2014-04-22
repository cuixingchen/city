package com.hdsx.taxi.woxing.cqcityserver;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.ArrayUtils;

public class LuncherTest {

	public static void main(String[] args) {
		ByteBuffer bf = ByteBuffer.allocate(1024);
		bf.put((byte) 0x1e);
		bf.putShort((short) 0x1e2b);
		bf.putShort((short) 0x1e2e);
		byte[] b = new byte[bf.position()];

		bf.position(0);
		bf.get(b);
		for (byte bb : b) {
			System.out.println(Integer.toHexString(bb));
		}
		System.out.println("******");
		bf.clear();
		bf.put((byte) 0x1f);
		bf.putShort((short) 0x1e2f);
		bf.putShort((short) 0x1f2e);
		bf.position(0);
		bf.get(b);
		for (byte bb : b) {
			System.out.println(Integer.toHexString(bb));
		}

		System.out.println(Integer.MAX_VALUE);
		long a = 360 * 60 * 10000;
		System.out.println(a);

	}
}
