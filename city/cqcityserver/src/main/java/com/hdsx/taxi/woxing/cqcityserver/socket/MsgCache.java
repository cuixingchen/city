package com.hdsx.taxi.woxing.cqcityserver.socket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.driver.cq.tcp.cache.MsgObj;
import com.hdsx.taxi.woxing.bean.util.CacheManagerUtil;
import com.hdsx.taxi.woxing.cqcityserver.socket.utils.DateFormateUtil;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg3003;

/**
 * 
 * @author Steven TODO TCP实现消息缓存
 */
public class MsgCache {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MsgCache.class);

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

	/**
	 * 存放消息
	 * 
	 * @param msg
	 */
	public void put(AbsMsg msg) {
		if (logger.isDebugEnabled()) {
			logger.debug("put(AbsMsg) - start"); //$NON-NLS-1$
		}

		if (msg.getHeader().getMsgid() == MessageID.msg0x0001
				|| msg.getHeader().getMsgid() == MessageID.msg0x0002
				|| msg.getHeader().getMsgid() == MessageID.msg0x0003) {
			if (logger.isDebugEnabled()) {
				logger.debug("put(AbsMsg) - end"); //$NON-NLS-1$
			}
			return;
		}
		int seq = msg.getHeader().getSn();

		String key = genKey(msg);
		MsgObj m = this.get(key);
		if (m == null)
			m = new MsgObj(msg);
		else {
			m.setSendtime(new Date());
			m.setSendedcount(m.getSendedcount() + 1);
		}
		this.put(m);

		if (logger.isDebugEnabled()) {
			logger.debug("消息加入缓存！消息key:" + seq);
		}
	}

	public void put(MsgObj msg) {
		AbsMsg m = msg.getMsg();
		String key = genKey(m);
		Element e = new Element(key, msg);
		this.cache.put(e);
	}

	public void remove(String key) {
		this.cache.remove(key);

	}

	public void remove(Msg3003 msg) {
		String key = getKey(msg);
		this.cache.remove(key);
	}

	/**
	 * 根据通用应答获取key
	 * 
	 * @param msg
	 * @return
	 */
	String getKey(Msg3003 msg) {
		String key = Integer.toHexString(msg.getMsgid()) + ";"
				+ msg.getHeader().getSn();
		return key;
	}

	/**
	 * 根据通用应答获取对应消息
	 * 
	 * @param msg
	 * @return
	 */
	public AbsMsg getMsgBy3003(Msg3003 msg) {
		String key = getKey(msg);
		return this.getMsg(key);

	}

	/**
	 * 生成消息key
	 * 
	 * @param msg
	 * @return
	 */
	String genKey(AbsMsg msg) {
		return Integer.toHexString(msg.getHeader().getMsgid()) + ";"
				+ msg.getHeader().getSn();
	}

	/**
	 * 根据返回消息 流水号+";"+id
	 * 
	 * @param key
	 * @return
	 */
	public AbsMsg getMsg(String key) {
		Element e = this.cache.get(key);
		MsgObj m = e == null ? null : (MsgObj) e.getObjectValue();
		return m == null ? null : m.getMsg();

	}

	/**
	 * 根据返回消息 流水号+";"+id
	 * 
	 * @param key
	 * @return
	 */
	public MsgObj get(String key) {
		Element e = this.cache.get(key);
		return e == null ? null : (MsgObj) e.getObjectValue();

	}

	/**
	 * 
	 * cleanAndgetResendMsg:(清理缓存中的消息，同时将需要重新发送的消息返回).
	 * 
	 * @author sid
	 * @param minInterval
	 *            最小间隔时间，以秒为单位
	 * @param maxCount
	 *            最大重发次数
	 * @param maxTime
	 *            最长缓存时间(小时)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MsgObj> cleanAndgetResendMsg(int minInterval, int maxCount,
			int maxTime) {
		List<MsgObj> list = new ArrayList<MsgObj>();

		List<String> keys = cache.getKeys();
		Date date = new Date();
		for (String key : keys) {
			MsgObj obj = MsgCache.getInstance().get(key);
			Date endtime = DateFormateUtil.getDateAddHours(obj.getCreatetime(),
					maxTime);

			if (obj.getSendedcount() < maxCount
					&& endtime.getTime() > date.getTime()) {
				long lasttime = obj.getSendtime().getTime();
				long now = System.currentTimeMillis();
				if ((now - lasttime) > (minInterval * 1000)) {
					list.add(obj);
				}
			} else
				MsgCache.getInstance().remove(key);
		}
		return list;

	}

}
