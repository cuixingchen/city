package com.hdsx.taxi.woxing.cqcityserver.socket;

import net.sf.ehcache.Ehcache;

import com.hdsx.taxi.woxing.cqmsg.msg.Msg0003;
import com.hdsx.taxi.woxing.nettyutil.IMsgCache;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;

/**
 * 
 * @author Steven TODO TCP实现消息缓存
 */
public class MsgCache implements IMsgCache {

	static MsgCache obj;

	public static MsgCache getInstance() {
		if (obj == null)
			obj = new MsgCache();
		return obj;
	}

	Ehcache canche;

	public void put(IMsg m) {

	}

	public void remove(Msg0003 msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(IMsg m) {
		// TODO Auto-generated method stub
		
	}

}
