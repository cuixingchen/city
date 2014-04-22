package com.hdsx.taxi.woxing.nettyutil;

import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;

public interface IMsgCache {
	public void put(IMsg m);

	public void remove(IMsg m);
}
