package com.hdsx.taxi.woxing.cqcityserver.socket;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.hdsx.taxi.woxing.bean.util.CacheManagerUtil;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0003;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;

/**
 * 
 * @author Steven TODO TCP实现消息缓存
 */
public class MsgCache {

	static MsgCache obj;

	public static MsgCache getInstance() {
		if (obj == null)
			obj = new MsgCache();
		return obj;
	}

	Ehcache cache;

	public MsgCache() {
		this.cache = CacheManagerUtil.getInstance().getCm()
				.getEhcache("tcpmsgcache");
	}

	public void put(AbsMsg m) {
		Element e = new Element(m.getHeader().getSn(), m);
		this.cache.put(e);
	}

	public void remove(Msg0003 msg) {
		// Element e = this.cache.get(msg.getHeader().getSn());
		// if (e == null)
		// return;
		this.cache.remove(msg.getHeader().getSn());

	}

	public void remove(IMsg m) {
		// TODO Auto-generated method stub

	}

	/**
	 * 根据流水号返回消息
	 * @param sn
	 * @return
	 */
	public AbsMsg getMsg(int sn) {
		Element e = this.cache.get(sn);
		if (e == null)
			return null;
		return (AbsMsg) e.getObjectValue();

	}

}
