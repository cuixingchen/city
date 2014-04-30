package com.hdsx.taxi.woxing.cqcityserver.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.socket.thread.CalTaxiIndexThread;
import com.hdsx.taxi.woxing.cqcityserver.socket.thread.HeartBeatThread;
import com.hdsx.taxi.woxing.cqcityserver.socket.thread.ReConnectedThread;
import com.hdsx.taxi.woxing.cqcityserver.socket.thread.ReSendMsgThread;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0001;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0003;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;

/**
 * tcp客戶端
 * 
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
	private static final Logger logger = LoggerFactory.getLogger(TcpClient.class);
	
	Channel ch;
	String hostname; // 服务器地址
	int hostport; // 服务器端口

	byte thirdflag; // 第三方登陆标识
	String vss;// 第三方接入vss
	int heartbeatdelay = 60;// 心跳间隔
	private long reconnectdealy = 60;
	private long resendmsgdealy = 60;

	ByteToMessageCodec<IMsg> codec;
	ChannelInboundHandlerAdapter handler;
	LogLevel loglevel;

	Bootstrap b;
	Channel channel;

	private boolean isLogined = false; // 是否登陆成功
	public ChannelHandlerContext chtx;
	public ChannelFuture cf;
	
	@Override
	public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		// String path =
		// TcpClient.class.getResource("/tcp.properties").getPath();
		Properties p = new Properties();
		try {
			p.load(TcpClient.class.getResourceAsStream("/tcp.properties"));

			this.hostname = p.getProperty("tcp.host");
			logger.info("run() - String hostname={}", hostname); //$NON-NLS-1$

			this.hostport = Integer.parseInt(p.getProperty("tcp.port"));

			String loglevelname = p.getProperty("tcp.loglevel").toUpperCase();
			logger.info("run() - String loglevelname={}", loglevelname); //$NON-NLS-1$

			this.loglevel = LogLevel.valueOf(loglevelname);

			this.thirdflag = Byte.parseByte(p.getProperty("tcp.thirdpartflag"));
			
			AbsMsg.THIRD_PART_FLAG = this.thirdflag; // 第三方接入平台标识
			this.vss = p.getProperty("tcp.vss"); // 第三方VSS
			Msg0001.VSS = this.vss;

			this.heartbeatdelay = Integer.parseInt(p.getProperty("tcp.heartbeatdelay"));
			this.reconnectdealy = Integer.parseInt(p.getProperty("tcp.reconnectdealy"));
			this.resendmsgdealy = Integer.parseInt(p.getProperty("tcp.resendmsgdealy"));

			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(new CQTcpCodec(),new LoggingHandler(loglevel),new TcpHandler());
						}
					});

			logger.debug("init(String, int, String, String) - ready to connect"); //$NON-NLS-1$

			b.connect(hostname, hostport).sync();

		} catch (InterruptedException | IOException e) {
			logger.error("init(String, int, String, String)", e); //$NON-NLS-1$
		} finally {
//			group.shutdownGracefully();
		}
	}
	/**
	 * 设置登陆状态
	 * 
	 * @param b
	 */
	public void loginOK(boolean b) {
		this.isLogined = b;

		if (b) {
			if (logger.isDebugEnabled()) {
				logger.debug("loginOK(boolean) - 启动线程"); //$NON-NLS-1$
			}
			new ReSendMsgThread().run(this.resendmsgdealy*1000, this.resendmsgdealy * 1000);// 启动发送缓存消息线程

			new HeartBeatThread().run(this.heartbeatdelay * 1000, this.heartbeatdelay * 1000);
			new ReConnectedThread().run(this.reconnectdealy * 1000,this.reconnectdealy * 1000);
			
			new CalTaxiIndexThread().run();
			if (logger.isDebugEnabled()) {
				logger.debug("loginOK(boolean) - 启动重新连接线程成功"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param m
	 */
	public void send(AbsMsg m) {
		// try {
		if (this.isLogined) {
			if (chtx != null && chtx.channel().isOpen()) {
				MsgCache.getInstance().put(m);
				ChannelFuture cf = chtx.write(m);
				chtx.flush();
				logger.debug("cf - :" + cf.toString() + cf.isSuccess());
			}
		}
	}

	/**
	 * 只发消息不缓存，用于心跳类消息
	 * 
	 * @param m
	 */
	public void sendWithoutCache(AbsMsg m) {
		if (isLogined) {
			if (chtx != null && chtx.channel().isOpen()) {
				ChannelFuture cf = chtx.write(m);
				chtx.flush();
				if (logger.isDebugEnabled()) {
					logger.debug("cf - :" + cf.toString() + cf.isSuccess());
				}
			}
		}
	}

	/**
	 * 
	 * login:(发送登陆消息). 
	 *
	 * @author sid
	 */
	public void login() {
		if (logger.isDebugEnabled()) {
			logger.debug("login() - start"); //$NON-NLS-1$
		}

		// 打开连接时发送登录消息
		try {

			Msg0001 loginmsg = new Msg0001();
			TcpClient.getInstance().send(loginmsg);

		} catch (Exception e) {
			logger.info("login() - Exception"+e.getStackTrace()); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("login() - end"); //$NON-NLS-1$
		}
	}

	public ChannelHandlerContext getChtx() {
		return chtx;
	}

	public void setChtx(ChannelHandlerContext chtx) {
		this.chtx = chtx;
	}

	public ChannelFuture getCf() {
		return cf;
	}

	public void setCf(ChannelFuture cf) {
		this.cf = cf;
	}
	public boolean isLogined() {
		return isLogined;
	}

	/**
	 * 发送收到消息的通用应答
	 * 
	 * @param msg
	 */
	public void sendAnsworMsg(AbsMsg msg) {
		Msg0003 mout = new Msg0003();
		mout.setMsgid(msg.getHeader().getMsgid());
		mout.getHeader().setSn(msg.getHeader().getSn());
		if (ch != null && ch.isOpen()) {
			ChannelFuture cf = ch.write(mout);
			logger.debug("cf - :" + cf.toString() + cf.isSuccess());
		}
	}

}
