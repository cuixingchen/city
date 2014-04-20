package com.hdsx.taxi.woxing.cityservice.service;

import com.hdsx.taxi.woxing.bean.Order;
import com.hdsx.taxi.woxing.bean.OrderResult;

/**
 * 订单接口
 * 
 * @author Steven
 * 
 */
public interface IOrderService {

	/**
	 * 提交订单
	 * 
	 * @param order
	 * @return
	 */
	public OrderResult onOrder(Order order);

	/**
	 * 取消订单
	 * 
	 * @param orderId
	 *            订单id
	 * @param cancelreason
	 *            取消原因
	 * @return 取消结果 null 取消成功 非空表示取消失败，并返回取消原因
	 */
	public String cancleorder(long orderId, String cancelreason);
}
