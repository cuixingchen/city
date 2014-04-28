package com.hdsx.taxi.woxing.cqcityserver.socket;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0001;
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
					handler.doHandle(m);
				}
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("channelActive(ChannelHandlerContext) - start"); //$NON-NLS-1$
		}

		super.channelActive(ctx);
		TcpClient.getInstance().setConnected(true);
		Msg0001 loginmsg = new Msg0001();
		ctx.channel().write(loginmsg);
		// TcpClient.getInstance().sendWithoutCache(loginmsg);

		if (logger.isDebugEnabled()) {
			logger.debug("channelActive(ChannelHandlerContext) - end"); //$NON-NLS-1$
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		TcpClient.getInstance().setConnected(false);
	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TcpHandler.class);

}
