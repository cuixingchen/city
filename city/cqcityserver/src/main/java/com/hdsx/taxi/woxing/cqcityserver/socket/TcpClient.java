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

import com.hdsx.taxi.woxing.cqcityserver.order.OrderContants;
import com.hdsx.taxi.woxing.cqcityserver.socket.thread.CalTaxiIndexThread;
import com.hdsx.taxi.woxing.cqcityserver.socket.thread.DoOrderHandleThread;
import com.hdsx.taxi.woxing.cqcityserver.socket.thread.HeartBeatThread;
import com.hdsx.taxi.woxing.cqcityserver.socket.thread.ParseMsgThreadManager;
import com.hdsx.taxi.woxing.cqcityserver.socket.thread.ReConnectedThread;
import com.hdsx.taxi.woxing.cqcityserver.socket.thread.ReSendMsgThread;
import com.hdsx.taxi.woxing.cqcityserver.socket.utils.TcpPropertiesUtil;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
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
	private static final Logger logger = LoggerFactory
			.getLogger(TcpClient.class);

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

	private int connstate = 0;

	public void reconnect() {

		logger.info("--------断线重连开始---------");
		if (this.connstate == 0)
			init();

	}

	@Override
	public void run() {
		init();
	}

	private void init() {
		EventLoopGroup group = new NioEventLoopGroup();
		// String path =
		// TcpClient.class.getResource("/tcp.properties").getPath();
		Properties p = new Properties();
		try {
			p.load(TcpClient.class.getResourceAsStream("/tcp.properties"));

			TcpPropertiesUtil.p = p;
			this.hostname = p.getProperty("tcp.host");

			this.hostport = Integer.parseInt(p.getProperty("tcp.port"));

			String loglevelname = p.getProperty("tcp.loglevel").toUpperCase();

			this.loglevel = LogLevel.valueOf(loglevelname);

			this.thirdflag = Byte.parseByte(p.getProperty("tcp.thirdpartflag"));

			AbsMsg.THIRD_PART_FLAG = this.thirdflag; // 第三方接入平台标识
			this.vss = p.getProperty("tcp.vss"); // 第三方VSS
			Msg0001.VSS = this.vss;

			this.heartbeatdelay = Integer.parseInt(p
					.getProperty("tcp.heartbeatdelay"));
			this.reconnectdealy = Integer.parseInt(p
					.getProperty("tcp.reconnectdealy"));
			this.resendmsgdealy = Integer.parseInt(p
					.getProperty("tcp.resendmsgdealy"));

			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(new CQTcpCodec(),
									new TcpHandler());
						}
					});

			logger.info("服务端地址：" + hostname + ":" + hostport); //$NON-NLS-1$

			cf = b.connect(hostname, hostport).sync();
			// cf.channel().closeFuture().sync();
			ParseMsgThreadManager.getInstance().run(0, 0);
		} catch (InterruptedException | IOException e) {
			logger.error("客户端初始化失败：", e); //$NON-NLS-1$
		} finally {
			// group.shutdownGracefully();
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

			logger.info("登陆成功开始启动消息重发、心跳、断线重连、打车指数、订单处理服务");
			ReSendMsgThread.getInstance().run(this.resendmsgdealy * 1000,
					this.resendmsgdealy * 1000);

			HeartBeatThread.getInstance().run(this.heartbeatdelay * 1000,
					this.heartbeatdelay * 1000);
			ReConnectedThread.getInstance().run(this.reconnectdealy * 1000,
					this.reconnectdealy * 1000);

			CalTaxiIndexThread.getInstance().run(0, 0);

			DoOrderHandleThread.getInstance().run(
					OrderContants.CALLTAXI_MINWAITINGTIME * 1000, 100);
			logger.info("---------初始化完成-------");
			this.connstate = 1;
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
				chtx.write(m);
				chtx.flush();
				logger.info("发送消息(已加入缓存)：" + m.toString());
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
				logger.info("发送消息：" + m.toString());
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
		// 打开连接时发送登录消息
		try {

			Msg0001 m = new Msg0001();

			if (chtx != null && chtx.channel().isOpen()) {
				MsgCache.getInstance().put(m);
				ChannelFuture cf = chtx.write(m);
				chtx.flush();
				logger.info("发送登陆消息：" + m.toString());
			}

		} catch (Exception e) {
			logger.error("登陆异常：", e);
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

	public int getConnstate() {
		return connstate;
	}

	public void setConnstate(int connstate) {
		this.connstate = connstate;
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
		short msgid = msg.getHeader().getMsgid();
		if (msgid == MessageID.msg0x3003) {
			return;
		}
		Msg0003 mout = new Msg0003();
		mout.setMsgid(msg.getHeader().getMsgid());
		mout.getHeader().setSn(msg.getHeader().getSn());
		if (chtx != null && chtx.channel().isOpen()) {
			ChannelFuture cf = chtx.write(mout);
			chtx.flush();
			logger.debug("发送通用应答:" + mout);
		}
	}

	public static void main(String[] args) {
		TcpClient.getInstance().run();
	}
}
