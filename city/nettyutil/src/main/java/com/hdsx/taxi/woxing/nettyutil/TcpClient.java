package com.hdsx.taxi.woxing.nettyutil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;

/**
 * tcp客戶端
 * @author Steven
 *
 */
public class TcpClient extends Thread {

	static TcpClient obj;

	public static TcpClient getInstance() {
		if (obj == null)
			obj = new TcpClient();
		return obj;
	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TcpClient.class);
	MsgCache msgcanche;
	Channel ch;
	boolean isLogined = false;
	String hostname; // 服务器地址
	int hostport; // 服务器端口
	ByteToMessageCodec<IMsg> codec;
	ChannelInboundHandlerAdapter handler;
	LogLevel loglevel;

	@Override
	public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(codec,
									new LoggingHandler(loglevel), handler);
						}
					});

			logger.debug("init(String, int, String, String) - ready to connect"); //$NON-NLS-1$

			b.connect(hostname, hostport).sync().channel().closeFuture().sync();

		} catch (InterruptedException e) {
			logger.error("init(String, int, String, String)", e); //$NON-NLS-1$
		} finally {
			group.shutdownGracefully();
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param m
	 */
	public void send(IMsg m) {
		// try {
		if (isLogined) {
			if (ch != null && ch.isOpen()) {
				msgcanche.put(m);
				ChannelFuture cf = ch.write(m);
				logger.debug("cf - :" + cf.toString() + cf.isSuccess());
			}
		}
	}

	/**
	 * 只发消息不缓存，用于心跳类消息
	 * 
	 * @param m
	 */
	public void sendWithoutCache(IMsg m) {
		if (isLogined) {
			if (ch != null && ch.isOpen()) {
				ChannelFuture cf = ch.write(m);
				logger.debug("cf - :" + cf.toString() + cf.isSuccess());

			}
		}

	}

}
