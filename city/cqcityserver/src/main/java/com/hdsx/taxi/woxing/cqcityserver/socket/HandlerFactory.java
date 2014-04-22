package com.hdsx.taxi.woxing.cqcityserver.socket;

import java.util.HashMap;

import com.hdsx.taxi.woxing.cqcityserver.socket.hanlder.Handler0002;
import com.hdsx.taxi.woxing.cqcityserver.socket.hanlder.Handler0003;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

public class HandlerFactory {

	static HashMap<Integer, IHandler> map = new HashMap<>();
	static {
		map.put(0x0003, new Handler0003());
		map.put(0x0002, new Handler0002());

	}

	public static IHandler getHandler(AbsMsg m) {

		return map.get(m.getHeader().getMsgid());
	}

}
