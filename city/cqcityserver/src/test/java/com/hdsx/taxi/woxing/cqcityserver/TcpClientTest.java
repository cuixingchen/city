package com.hdsx.taxi.woxing.cqcityserver;


import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0001;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0002;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0003;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1001;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1002;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1003;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1004;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1005;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1006;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1007;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1010;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1011;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1012;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1013;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1014;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1015;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1016;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1101;

public class TcpClientTest {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TcpClientTest.class);

	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		TcpClient.getInstance().run();
		int i = 0;
		while (true) {
			i++;
			if (logger.isDebugEnabled()) {
				logger.debug("while(true) - i:"+i); //$NON-NLS-1$
			}
			Scanner in = new Scanner(System.in);
			int msgID = in.nextInt();
			AbsMsg m = null;
			switch (msgID) {
			case MessageID.msg0x0001:
				m = new Msg0001();
				break;
			case MessageID.msg0x0002:
				m = new Msg0002();
				break;
			case MessageID.msg0x0003:
				m = new Msg0003();
				break;
			case MessageID.msg0x1001:
				m = new Msg1001();
				break;
			case MessageID.msg0x1002:
				m = new Msg1002();
				break;
			case MessageID.msg0x1003:
				m = new Msg1003();
				break;
			case MessageID.msg0x1004:
				m = new Msg1004();
				break;
			case MessageID.msg0x1005:
				m = new Msg1005();
				break;
			case MessageID.msg0x1006:
				m = new Msg1006();
				break;
			case MessageID.msg0x1007:
				m = new Msg1007();
				break;
			case MessageID.msg0x1010:
				m = new Msg1010();
				break;
			case MessageID.msg0x1011:
				m = new Msg1011();
				break;
			case MessageID.msg0x1012:
				m = new Msg1012();
				break;
			case MessageID.msg0x1013:
				m = new Msg1013();
				break;
			case MessageID.msg0x1014:
				m = new Msg1014();
				break;
			case MessageID.msg0x1015:
				m = new Msg1015();
				break;
			case MessageID.msg0x1016:
				m = new Msg1016();
				break;
			case MessageID.msg0x1101:
				m = new Msg1101();
				break;
			default:
				break;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error("main(String[])", e); //$NON-NLS-1$

				e.printStackTrace();
			}
			TcpClient.getInstance().send(m);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				logger.error("main(String[])", e); //$NON-NLS-1$

				e.printStackTrace();
			}
			
		}
	}
}
