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
		if (logger.isDebugEnabled()) {
			logger.debug("channelRead(ChannelHandlerContext, Object) - start"); //$NON-NLS-1$
		}

		try {
			if (msg instanceof AbsMsg) {

				final AbsMsg m = (AbsMsg) msg;

				new Thread(new Runnable() {

					@Override
					public void run() {
						IHandler handler = HandlerFactory.getHandler(m);
						if (handler != null) {
							handler.doHandle(m);
						}

					}
				}).run();
				TcpClient.getInstance().sendAnsworMsg(m);
			}

		} finally {
			ReferenceCountUtil.release(msg);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("channelRead(ChannelHandlerContext, Object) - end"); //$NON-NLS-1$
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("channelActive(ChannelHandlerContext) - start"); //$NON-NLS-1$
		}

		super.channelActive(ctx);
		TcpClient.getInstance().setChtx(ctx);
		TcpClient.getInstance().login();

		if (logger.isDebugEnabled()) {
			logger.debug("channelActive(ChannelHandlerContext) - end"); //$NON-NLS-1$
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		TcpClient.getInstance().getChtx().close();
		TcpClient.getInstance().setConnstate(0);
	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TcpHandler.class);

}
