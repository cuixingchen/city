package com.hdsx.taxi.woxing.cqcityserver.socket;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息队列
 * 
 * @author cuipengfei
 * 
 */
public class MsgQueue {

	/**
	 * 接收数据队列
	 */
	private static LinkedBlockingQueue<byte[]> rec_queue = new LinkedBlockingQueue<byte[]>();

	public static LinkedBlockingQueue<byte[]> getRecqueue() {
		return rec_queue;
	}
}
