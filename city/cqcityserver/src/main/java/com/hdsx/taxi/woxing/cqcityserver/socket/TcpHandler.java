package com.hdsx.taxi.woxing.cqcityserver.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
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
			if (msg instanceof byte[]) {

				final byte[] msgbytes = (byte[]) msg;
				try {
					MsgQueue.getRecqueue().put(msgbytes);
				} catch (InterruptedException e) {
					logger.error("主handler---接收消息队列存储消息失败", e);
				}
			}else {
				logger.error("主handler---消息解码有误，请检查！！");
			}

		} finally {
			ReferenceCountUtil.release(msg);
		}

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		super.channelActive(ctx);
		logger.info("-------------临时连接建立--------------");
		TcpClient.getInstance().setChtx(ctx);
		TcpClient.getInstance().login();

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		logger.info("-------------连接断开--------------");
		TcpClient.getInstance().getChtx().close();
		TcpClient.getInstance().setConnstate(0);
	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TcpHandler.class);

}
