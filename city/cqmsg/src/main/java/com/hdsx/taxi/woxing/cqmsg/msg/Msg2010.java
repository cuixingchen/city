package com.hdsx.taxi.woxing.cqmsg.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.hdsx.taxi.woxing.bean.util.coor.CoordinateCodec;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.Converter;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.DriverInfo;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.TaxiPointInfo;

/**
 * 车辆位置同步信息
 * 
 * @author Steven
 * 
 */
public class Msg2010 extends AbsMsg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Msg2010.class);

	List<TaxiPointInfo> list = new ArrayList<>();

	@Override
	protected int getMsgID() {

		return 0x2010;
	}

	public List<TaxiPointInfo> getList() {
		return list;
	}

	public void setList(List<TaxiPointInfo> list) {
		this.list = list;
	}

	@Override
	protected byte[] bodytoBytes() {
		ByteBuffer b = ByteBuffer.allocate(4096); // 1 kb 缓冲区

		for (TaxiPointInfo cp : list) {

			// begin 司机

			ByteBuffer b_name = ByteBuffer.allocate(16);
			b_name.put(Converter.getBytes(cp.getDriver().getDriverName()));
			b.put(b_name.array());

			ByteBuffer b_company = ByteBuffer.allocate(16);
			b_company.put(Converter.getBytes(cp.getDriver().getCompany()));
			b.put(b_company.array());

			ByteBuffer b_phone = ByteBuffer.allocate(11);

			b_phone.put(Converter.getBytes(cp.getDriver().getDriverPhone()));
			b.put(b_phone.array());

			ByteBuffer b_serial = ByteBuffer.allocate(19);
			b_serial.put(Converter.getBytes(cp.getDriver().getDriverSerial()));
			b.put(b_serial.array());

			b.put(cp.getDriver().getCreditLevel());

			ByteBuffer b_number = ByteBuffer.allocate(8);
			b_number.put(Converter.getBytes(cp.getDriver().getLicenseNumber()));
			b.put(b_number.array());

			// end 司机信息

			long lon = CoordinateCodec.Coor2UInt(cp.getLon());
			
			b.put(Converter.unSigned32LongToBigBytes(CoordinateCodec
					.Coor2UInt(cp.getLon())));

			b.put(Converter.unSigned32LongToBigBytes(CoordinateCodec
					.Coor2UInt(cp.getLat())));

			// b.put(Converter.unSigned32LongToBigBytes(cp.getLon()));
			// b.put(Converter.unSigned32LongToBigBytes(cp.getLat()));

			b.put(cp.getState());
		}

		// 把当前 buffer 内容转换成 byte []
		byte[] result = new byte[b.position()];
		b.position(0);
		b.get(result);
		// 返回 新的 byte []
		return result;
	}

	protected boolean bodyfrombytes(byte[] b) {

		try {

			ByteBuffer bf = ByteBuffer.wrap(b);
			int offset = this.head.getLength();

			list = new ArrayList<TaxiPointInfo>();

			for (int i = 0; i < b.length / 80; i++) {

				TaxiPointInfo cp = new TaxiPointInfo();

				DriverInfo di = new DriverInfo();

				di.setDriverName(Converter.toGBKString(b, offset, 16));
				offset += 16;

				di.setCompany(Converter.toGBKString(b, offset, 16));
				offset += 16;

				di.setDriverPhone(Converter.toGBKString(b, offset, 11));
				offset += 11;

				di.setDriverSerial(Converter.toGBKString(b, offset, 19));
				offset += 19;

				di.setCreditLevel(bf.get(offset));
				offset += 1;

				di.setLicenseNumber(Converter.toGBKString(b, offset, 8));
				offset += 8;

				cp.setDriver(di);

				long lon = Converter.bytes2Unsigned32Long(b, offset);
		
				cp.setLon(CoordinateCodec.Coor2Float(lon));
				offset += 4;

				long lat = Converter.bytes2Unsigned32Long(b, offset);
				cp.setLat(CoordinateCodec.Coor2Float(Converter
						.bytes2Unsigned32Long(b, offset)));
				// cp.setLat(Converter.toUInt32(b, offset));
				offset += 4;

				cp.setState(bf.get(offset));
				offset += 1;

				list.add(cp);

			}

			return true;
		} catch (Exception ex) {

			ex.getStackTrace();
		}
		return false;

	}

	@Override
	public String toString() {
		return "Msg2010 [list=" + list + "]";
	}

}
