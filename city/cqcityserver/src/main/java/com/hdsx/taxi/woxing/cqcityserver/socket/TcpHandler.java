package com.hdsx.taxi.woxing.cqcityserver.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

/**
 * tcp连接后的事件处理
 * 
 * @author Steven
 * 
 */
public class TcpHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {

		try {
			if (msg.getClass().isInstance(AbsMsg.class)) {
				AbsMsg m = (AbsMsg) msg;
				IHandler handler = HandlerFactory.getHandler(m);
				if (handler != null) {
					// TODO 完成消息的匹配 handler.doHandle(m);
				}
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TcpHandler.class);

}
