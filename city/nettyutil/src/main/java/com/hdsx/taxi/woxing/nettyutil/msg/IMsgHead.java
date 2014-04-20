package com.hdsx.taxi.woxing.nettyutil.msg;

public interface IMsgHead {
	byte[] tobytes();

	int getHeadLen();

	void frombytes(byte[] b);
}
